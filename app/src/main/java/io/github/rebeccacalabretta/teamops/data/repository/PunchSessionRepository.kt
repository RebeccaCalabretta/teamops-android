package io.github.rebeccacalabretta.teamops.data.repository

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import kotlinx.coroutines.flow.Flow

interface PunchSessionRepository {
    suspend fun checkIn(objectId: String)

    suspend fun checkOut(
        endLocation: Location,
        objectEntity: ObjectEntity
    )
    suspend fun getOpenSessionOrNull(): PunchSessionEntity?

    fun getLatestSessions(limit: Int = 20): Flow<List<PunchSessionEntity>>

    fun getSessionsForMonth(monthKey: String): Flow<List<PunchSessionEntity>>
}