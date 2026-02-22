package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleEntity(
    @PrimaryKey val id: String,
    val employeeId: String,
    val objectId: String,
    val date: Long,
    val startTime: Long,
    val endTime: Long,
    val createdBy: String,
    val lastEditedBy: String? = null,
    val lastEditedAt: Long? = null
    )