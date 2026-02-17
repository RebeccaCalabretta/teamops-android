package io.github.rebeccacalabretta.teamops.data.sample

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import java.time.LocalDate

object SampleVacations {

    val items = listOf(
        VacationEntry(
            id = "vac_001",
            employeeId = "emp_001",
            startDate = LocalDate.now().plusDays(10),
            endDate = LocalDate.now().plusDays(15),
            status = VacationStatus.APPROVED
        ),
        VacationEntry(
            id = "vac_002",
            employeeId = "emp_002",
            startDate = LocalDate.now().plusDays(20),
            endDate = LocalDate.now().plusDays(25),
            status = VacationStatus.REQUESTED
        )
    )
}