package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PunchSessionEntity::class, ObjectEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun punchSessionDao(): PunchSessionDao
    abstract fun objectDao(): ObjectDao
}