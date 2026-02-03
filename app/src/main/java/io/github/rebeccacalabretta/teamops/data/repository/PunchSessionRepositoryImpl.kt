package io.github.rebeccacalabretta.teamops.data.repository

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionDao
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.util.GeoDistance
import io.github.rebeccacalabretta.teamops.util.MonthKey
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import java.time.ZoneId

class PunchSessionRepositoryImpl(
    private val dao: PunchSessionDao
) : PunchSessionRepository {
    override suspend fun checkIn(objectId: String, employeeId: String) {
        val open = dao.getOpenSessionOrNull()
        if (open != null) throw IllegalStateException("There is already an open session.")

        val now = System.currentTimeMillis()
        dao.insert(
            PunchSessionEntity(
                objectId = objectId,
                employeeId = employeeId,
                startTime = now,
                endTime = null,
                monthKey = MonthKey.fromTimestamp(now)
            )
        )
    }

    override suspend fun checkOut(
        endLocation: Location,
        objectEntity: ObjectEntity
    ) {
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

        if (duration < 60_000) {
            dao.delete(open)
        } else {
            dao.update(
                open.copy(
                    endTime = endTime,
                    checkOutDistanceMeters = distance
                    )
            )
        }
    }

    override suspend fun getOpenSessionOrNull(): PunchSessionEntity? =
        dao.getOpenSessionOrNull()


    override fun getLatestSessions(limit: Int): Flow<List<PunchSessionEntity>> =
        dao.getLatestSessions(limit)


    override fun getSessionsForMonth(yearMonth: YearMonth): Flow<List<PunchSessionEntity>> {
        val zone = ZoneId.systemDefault()

        val fromMillis = yearMonth
            .atDay(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        val toMillis = yearMonth
            .plusMonths(1)
            .atDay(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        return dao.getSessionBetween(fromMillis, toMillis)
    }

    override fun getSessionsForEmployee(employeeId: String): Flow<List<PunchSessionEntity>> =
        dao.getSessionsForEmployee(employeeId)
}