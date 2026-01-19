package io.github.rebeccacalabretta.teamops.ui.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PunchSessionDao {

    @Query("SELECT * FROM punch_sessions WHERE endTime IS NULL LIMIT 1")
    suspend fun getOpenSessionOrNull(): PunchSessionEntity?

    @Query("SELECT * FROM punch_sessions ORDER BY startTime DESC")
    fun getAllSessionsDesc(): Flow<List<PunchSessionEntity>>

    @Query("SELECT * FROM punch_sessions ORDER BY startTime DESC LIMIT :limit")
    fun getLatestSessions(limit: Int): Flow<List<PunchSessionEntity>>

    @Query("""
        SELECT * FROM punch_sessions
        WHERE monthKey = :monthKey
        ORDER BY startTime DESC
    """)
    fun getSessionsForMonth(monthKey: String): Flow<List<PunchSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(session: PunchSessionEntity)

    @Update
    suspend fun update(session: PunchSessionEntity)
}