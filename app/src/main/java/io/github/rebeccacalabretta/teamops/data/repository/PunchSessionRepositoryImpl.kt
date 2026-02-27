package io.github.rebeccacalabretta.teamops.data.repository

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionDao
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.data.remote.PunchSessionDocument
import io.github.rebeccacalabretta.teamops.data.remote.RemotePunchDataSource
import io.github.rebeccacalabretta.teamops.util.GeoDistance
import io.github.rebeccacalabretta.teamops.util.MonthKey
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import java.time.ZoneId

class PunchSessionRepositoryImpl(
    private val dao: PunchSessionDao,
    private val remote: RemotePunchDataSource
) : PunchSessionRepository {

    private fun PunchSessionEntity.toDocument(
        createdAt: Long = startTime,
        lastEditedAt: Long? = null,
        lastEditedBy: String? = null
    ): PunchSessionDocument = PunchSessionDocument(
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

    private suspend fun runRemote(context: String, block: suspend () -> Unit) {
        try { block() } catch (e: Exception) {
            throw IllegalStateException("Remote failed ($context)", e)
        }
    }

    private suspend fun runLocal(context: String, block: suspend () -> Unit) {
        try { block() } catch (e: Exception) {
            throw IllegalStateException("Local failed ($context)", e)
        }
    }

    override suspend fun checkIn(objectId: String, employeeId: String) {
        val open = dao.getOpenSessionOrNull()
        if (open != null) throw IllegalStateException("There is already an open session.")

        val now = System.currentTimeMillis()
        val entity = PunchSessionEntity(
            objectId = objectId,
            employeeId = employeeId,
            startTime = now,
            endTime = null,
            monthKey = MonthKey.fromTimestamp(now)
        )

        runRemote("checkIn id=${entity.id}") {
            remote.upsert(entity.toDocument(createdAt = now))
        }
        runLocal("checkIn id=${entity.id}") {
            dao.insert(entity)
        }
    }

    override suspend fun checkOut(endLocation: Location, objectEntity: ObjectEntity) {
        val open = dao.getOpenSessionOrNull()
            ?: throw IllegalStateException("No open session to check out.")

        val endTime = System.currentTimeMillis()
        val duration = endTime - open.startTime

        val distance = GeoDistance.distanceInMeters(
            lat1 = endLocation.latitude,
            lon1 = endLocation.longitude,
            lat2 = objectEntity.latitude,
            lon2 = objectEntity.longitude
        )

        if (duration < 60_000L) {
            runRemote("short delete id=${open.id}") { remote.delete(open.id) }
            runLocal("short delete id=${open.id}") { dao.delete(open) }
            return
        }

        val updated = open.copy(
            endTime = endTime,
            checkOutDistanceMeters = distance
        )

        runRemote("checkOut id=${updated.id}") {
            remote.upsert(
                updated.toDocument(
                    createdAt = open.startTime,
                    lastEditedAt = endTime,
                    lastEditedBy = open.employeeId
                )
            )
        }
        runLocal("checkOut id=${updated.id}") {
            dao.update(updated)
        }
    }

    override suspend fun updateSession(session: PunchSessionEntity) {
        val now = System.currentTimeMillis()

        runRemote("updateSession id=${session.id}") {
            remote.upsert(
                session.toDocument(
                    createdAt = session.startTime,
                    lastEditedAt = now,
                    lastEditedBy = session.employeeId
                )
            )
        }
        runLocal("updateSession id=${session.id}") {
            dao.update(session)
        }
    }

    override suspend fun getOpenSessionOrNull(): PunchSessionEntity? =
        dao.getOpenSessionOrNull()

    override fun getLatestSessions(limit: Int): Flow<List<PunchSessionEntity>> =
        dao.getLatestSessions(limit)

    override fun getSessionsForMonth(yearMonth: YearMonth): Flow<List<PunchSessionEntity>> =
        dao.getSessionBetween(
            fromMillis = yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            toMillis = yearMonth.plusMonths(1).atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )

    override fun getSessionsForEmployee(employeeId: String): Flow<List<PunchSessionEntity>> =
        dao.getSessionsForEmployee(employeeId)
}