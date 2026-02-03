package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PunchSessionDao {

    @Query("SELECT * FROM punch_sessions WHERE endTime IS NULL LIMIT 1")
    suspend fun getOpenSessionOrNull(): PunchSessionEntity?

    @Query(
        """
            SELECT * FROM punch_sessions
            WHERE startTime >= :fromMillis
            AND startTime < :toMillis
            ORDER BY startTime ASC
        """
    )
    fun getSessionBetween(
        fromMillis: Long,
        toMillis: Long
    ): Flow<List<PunchSessionEntity>>

    @Query(
        """
            SELECT * FROM punch_sessions
            WHERE employeeId = :employeeId
            ORDER BY startTime DESC
        """
    )
    fun getSessionsForEmployee(
        employeeId: String
    ): Flow<List<PunchSessionEntity>>


    @Query(
        """
            SELECT * FROM (
                SELECT * FROM punch_sessions
                ORDER BY startTime DESC
                LIMIT :limit
            )
            ORDER BY startTime ASC
        """
    )
    fun getLatestSessions(limit: Int = 20): Flow<List<PunchSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(session: PunchSessionEntity)

    @Update
    suspend fun update(session: PunchSessionEntity)

    @Delete
    suspend fun delete(session: PunchSessionEntity)
}