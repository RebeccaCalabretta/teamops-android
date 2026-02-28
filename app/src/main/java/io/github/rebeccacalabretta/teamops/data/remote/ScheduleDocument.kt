package io.github.rebeccacalabretta.teamops.data.remote

data class ScheduleDocument(
    val id: String = "",
    val employeeId: String = "",
    val objectId: String = "",
    val date: Long = 0L,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val createdBy: String = "",
    val lastEditedBy: String? = null,
    val lastEditedAt: Long? = null
)