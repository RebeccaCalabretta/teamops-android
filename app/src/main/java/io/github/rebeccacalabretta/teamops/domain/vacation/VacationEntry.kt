package io.github.rebeccacalabretta.teamops.domain.vacation

import java.time.LocalDate

data class VacationEntry(
    val id: String,
    val employeeId: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: VacationStatus
)