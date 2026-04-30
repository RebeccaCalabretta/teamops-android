package io.github.rebeccacalabretta.teamops.data.repository

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface PunchRepository {
    suspend fun checkIn(
        objectId: String,
        employeeId: String,
        currentUserId: String
    )

    suspend fun checkOut(
        endLocation: Location,
        objectEntity: ObjectEntity,
        currentUserId: String
    )

    suspend fun checkOutWithoutLocation(
        currentUserId: String
    )

    suspend fun getOpenSessionOrNull(): PunchSessionEntity?

    suspend fun updateSession(
        session: PunchSessionEntity,
        currentUserId: String
    )

    fun getSessionsForMonth(yearMonth: YearMonth): Flow<List<PunchSessionEntity>>

    fun getLatestSessions(limit: Int = 20): Flow<List<PunchSessionEntity>>

    fun getSessionsForEmployee(employeeId: String): Flow<List<PunchSessionEntity>>

    fun observeAndSyncSessions(employeeId: String): Flow<List<PunchSessionEntity>>
}