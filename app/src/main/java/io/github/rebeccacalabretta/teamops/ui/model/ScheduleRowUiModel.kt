package io.github.rebeccacalabretta.teamops.ui.model

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleRowUiModel(
    val id: String,
    val employeeId: String,
    val objectId: String,
    val objectName: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)