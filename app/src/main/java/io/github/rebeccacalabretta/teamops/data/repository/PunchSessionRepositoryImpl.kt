package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.PunchSessionDao
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.util.MonthKey
import kotlinx.coroutines.flow.Flow

class PunchSessionRepositoryImpl(
    private val dao: PunchSessionDao
) : PunchSessionRepository {
    override suspend fun checkIn(objectId: String) {
        val open = dao.getOpenSessionOrNull()
        if (open != null) throw IllegalStateException("There is already an open session.")

        val now = System.currentTimeMillis()
        dao.insert(
            PunchSessionEntity(
                objectId = objectId,
                startTime = now,
                endTime = null,
                monthKey = MonthKey.fromTimestamp(now)
            )
        )
    }

    override suspend fun checkOut() {
        val open = dao.getOpenSessionOrNull()
            ?: throw IllegalStateException("No open session to check out.")

        val endTime = System.currentTimeMillis()
        val duration = endTime - open.startTime
        if (duration < 60_000) {
            dao.delete(open)
        } else {
            dao.update(
                open.copy(endTime = endTime)
            )
        }
    }

    override suspend fun getOpenSessionOrNull(): PunchSessionEntity? =
        dao.getOpenSessionOrNull()


    override fun getLatestSessions(limit: Int): Flow<List<PunchSessionEntity>> =
        dao.getLatestSessions(limit)


    override fun getSessionsForMonth(monthKey: String): Flow<List<PunchSessionEntity>> =
        dao.getSessionsForMonth(monthKey)

}