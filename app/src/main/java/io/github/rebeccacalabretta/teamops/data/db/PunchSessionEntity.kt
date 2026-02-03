package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "punch_sessions")
data class PunchSessionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val objectId: String,
    val employeeId: String,

    val startTime: Long,
    val endTime: Long? = null,

    val startLatitude: Double? = null,
    val startLongitude: Double? = null,
    val startAccuracyMeters: Float? = null,
    val startDistanceMeters: Float? = null,
    val startWithinRadius: Boolean? = null,

    val endLatitude: Double? = null,
    val endLongitude: Double? = null,
    val endAccuracyMeters: Float? = null,
    val endDistanceMeters: Float? = null,
    val endWithinRadius: Boolean? = null,

    val monthKey: String,

    val checkOutDistanceMeters: Double? = null
)