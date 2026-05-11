package io.github.rebeccacalabretta.teamops.data.repository

import android.location.Location
import android.util.Log
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionDao
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.data.remote.PunchSessionDataSource
import io.github.rebeccacalabretta.teamops.data.remote.PunchSessionDocument
import io.github.rebeccacalabretta.teamops.util.geo.GeoDistance
import io.github.rebeccacalabretta.teamops.util.time.MonthKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.ZoneId

class PunchRepositoryImpl(
    private val dao: PunchSessionDao,
    private val remote: PunchSessionDataSource
) : PunchRepository {

    private fun PunchSessionEntity.toDocument(
        createdAt: Long = startTime,
        lastEditedAt: Long? = null,
        lastEditedBy: String? = null
    ): PunchSessionDocument =
        PunchSessionDocument(
            id = id,
            employeeId = employeeId,
            objectId = objectId,
            startTime = startTime,
            endTime = endTime,
            createdAt = createdAt,
            lastEditedAt = lastEditedAt,
            lastEditedBy = lastEditedBy,
            checkOutDistanceMeters = checkOutDistanceMeters
        )

    override suspend fun checkIn(
        objectId: String,
        employeeId: String,
        currentUserId: String
    ) {
        val open = dao.getOpenSessionOrNull()
        if (open != null) throw IllegalStateException("OPEN_SESSION_EXISTS")

        val now = System.currentTimeMillis()

        val entity = PunchSessionEntity(
            objectId = objectId,
            employeeId = employeeId,
            startTime = now,
            endTime = null,
            monthKey = MonthKey.fromTimestamp(now)
        )

        remote.upsert(
            entity.toDocument(
                createdAt = now,
                lastEditedAt = null,
                lastEditedBy = null
            )
        )

        dao.insert(entity)
    }

    override suspend fun checkOut(
        endLocation: Location,
        objectEntity: ObjectEntity,
        currentUserId: String
    ) {
        val distance = GeoDistance.distanceInMeters(
            lat1 = endLocation.latitude,
            lon1 = endLocation.longitude,
            lat2 = objectEntity.latitude,
            lon2 = objectEntity.longitude
        )

        finishOpenSession(
            currentUserId = currentUserId,
            checkOutDistanceMeters = distance
        )
    }

    override suspend fun checkOutWithoutLocation(
        currentUserId: String
    ) =
        finishOpenSession(
            currentUserId = currentUserId,
            checkOutDistanceMeters = null
        )

    private suspend fun finishOpenSession(
        currentUserId: String,
        checkOutDistanceMeters: Double?
    ) {
        val open = dao.getOpenSessionOrNull()
            ?: throw IllegalStateException("NO_OPEN_SESSION")

        val endTime = System.currentTimeMillis()
        val duration = endTime - open.startTime

        if (duration < 60_000L) {
            remote.delete(open.id)
            dao.delete(open)
            return
        }

        val updated = open.copy(
            endTime = endTime,
            checkOutDistanceMeters = checkOutDistanceMeters
        )

        remote.upsert(
            updated.toDocument(
                createdAt = open.startTime,
                lastEditedAt = endTime,
                lastEditedBy = currentUserId
            )
        )

        dao.update(updated)
    }

    override suspend fun updateSession(
        session: PunchSessionEntity,
        currentUserId: String
    ) {
        val now = System.currentTimeMillis()

        remote.upsert(
            session.toDocument(
                createdAt = session.startTime,
                lastEditedAt = now,
                lastEditedBy = currentUserId
            )
        )

        dao.update(session)
    }

    override suspend fun getOpenSessionOrNull(): PunchSessionEntity? =
        dao.getOpenSessionOrNull()

    override fun getLatestSessions(limit: Int): Flow<List<PunchSessionEntity>> =
        dao.getLatestSessions(limit)

    override fun getSessionsForMonth(yearMonth: YearMonth): Flow<List<PunchSessionEntity>> =
        dao.getSessionBetween(
            fromMillis = yearMonth.atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            toMillis = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

    override fun getSessionsForEmployee(employeeId: String): Flow<List<PunchSessionEntity>> =
        dao.getSessionsForEmployee(employeeId)

    override fun observeAndSyncSessions(employeeId: String): Flow<List<PunchSessionEntity>> =
        remote.observePunchSessions(employeeId).map { docs ->
            Log.d("PunchSync", "observeAndSyncSessions: employeeId=$employeeId, docs=${docs.size}")

            val entities = docs.map { it.toEntity() }

            Log.d("PunchSync", "mapped entities=${entities.size}")

            dao.insertAllReplace(entities)

            Log.d("PunchSync", "insertAllReplace finished")

            entities
        }

    private fun PunchSessionDocument.toEntity(): PunchSessionEntity =
        PunchSessionEntity(
            id = id,
            employeeId = employeeId,
            objectId = objectId,
            startTime = startTime,
            endTime = endTime,
            monthKey = MonthKey.fromTimestamp(startTime),
            checkOutDistanceMeters = checkOutDistanceMeters
        )
}