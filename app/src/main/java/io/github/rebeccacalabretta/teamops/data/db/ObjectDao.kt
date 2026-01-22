package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectDao {

    @Query("SELECT * FROM objects")
    fun getAllObjects(): Flow<List<ObjectEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(objects: List<ObjectEntity>)
}