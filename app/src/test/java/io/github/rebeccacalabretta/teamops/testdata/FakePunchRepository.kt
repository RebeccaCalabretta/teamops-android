package io.github.rebeccacalabretta.teamops.testdata

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.data.repository.PunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.YearMonth

class FakePunchRepository : PunchRepository {

    private var openSession: PunchSessionEntity? = null
    private val sessions = mutableListOf<PunchSessionEntity>()
    var checkInCalled = false

    override suspend fun checkIn(
        objectId: String,
        employeeId: String,
        currentUserId: String
    ) {
        checkInCalled = true

        if (openSession != null) throw IllegalStateException("OPEN_SESSION_EXISTS")

        val now = System.currentTimeMillis()

        val session = PunchSessionEntity(
            objectId = objectId,
            employeeId = employeeId,
            startTime = now,
            endTime = null,
            monthKey = "test-month"
        )

        openSession = session
        sessions.add(session)
    }

    override suspend fun checkOut(
        endLocation: Location,
        objectEntity: ObjectEntity,
        currentUserId: String
    ) {
        val open = openSession ?: throw IllegalStateException("NO_OPEN_SESSION")

        val updated = open.copy(
            endTime = System.currentTimeMillis()
        )

        openSession = null
        sessions.remove(open)
        sessions.add(updated)
    }

    override suspend fun getOpenSessionOrNull(): PunchSessionEntity? {
        return openSession
    }

    override suspend fun updateSession(
        session: PunchSessionEntity,
        currentUserId: String
    ) {
        sessions.removeIf { it.id == session.id }
        sessions.add(session)
    }

    override fun getSessionsForMonth(yearMonth: YearMonth): Flow<List<PunchSessionEntity>> {
        return flowOf(sessions)
    }

    override fun getLatestSessions(limit: Int): Flow<List<PunchSessionEntity>> {
        return flowOf(sessions.take(limit))
    }

    override fun getSessionsForEmployee(employeeId: String): Flow<List<PunchSessionEntity>> {
        return flowOf(
            sessions.filter { it.employeeId == employeeId }
        )
    }
}