package io.github.rebeccacalabretta.teamops.domain.repository

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface VacationRepository {

    fun observeVacationsForEmployee(
        employeeId: String
    ): Flow<List<VacationEntry>>

    suspend fun submitVacationRequest(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        requestedBy: String
    )
}