package io.github.rebeccacalabretta.teamops.domain.schedule

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleEntry(
    val id: String,
    val employeeId: String,
    val objectId: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val notes: String? = null
)