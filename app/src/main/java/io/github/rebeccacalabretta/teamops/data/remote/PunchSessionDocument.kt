package io.github.rebeccacalabretta.teamops.data.remote

data class PunchSessionDocument(
    val id: String = "",
    val employeeId: String = "",
    val objectId: String = "",
    val startTime: Long = 0L,
    val endTime: Long? = null,
    val createdAt: Long = 0L,
    val lastEditedAt: Long? = null,
    val lastEditedBy: String? = null,
    val checkOutDistanceMeters: Double? = null
)