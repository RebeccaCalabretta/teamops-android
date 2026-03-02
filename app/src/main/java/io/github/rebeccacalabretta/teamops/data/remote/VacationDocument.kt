package io.github.rebeccacalabretta.teamops.data.remote

data class VacationDocument(
    val id: String = "",
    val employeeId: String = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val daysRequested: Int = 0,
    val status: String = "",
    val createdAt: Long = 0L,
    val decidedAt: Long? = null,
    val decidedBy: String? = null
)