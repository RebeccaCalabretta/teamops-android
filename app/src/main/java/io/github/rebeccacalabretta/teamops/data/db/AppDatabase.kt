package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PunchSessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun punchSessionDao(): PunchSessionDao
}