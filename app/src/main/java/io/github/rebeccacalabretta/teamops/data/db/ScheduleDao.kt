package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query(
        """
            SELECT * FROM schedule
            WHERE employeeId = :employeeId
            ORDER BY date ASC, startTime ASC
        """
    )
    fun getScheduleForEmployee(employeeId: String): Flow<List<ScheduleEntity>>

    @Upsert
    suspend fun upsertSchedule(entry: ScheduleEntity)

    @Upsert
    suspend fun upsertAll(entries: List<ScheduleEntity>)

    @Query("DELETE FROM schedule WHERE id = :scheduleId")
    suspend fun deleteSchedule(scheduleId: String)
}