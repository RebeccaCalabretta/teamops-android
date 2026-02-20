package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getScheduleForEmployee(employeeId: String): Flow<List<ScheduleEntity>>

    suspend fun upsertSchedule(entry: ScheduleEntity)

    suspend fun deleteSchedule(entry: ScheduleEntity)
}