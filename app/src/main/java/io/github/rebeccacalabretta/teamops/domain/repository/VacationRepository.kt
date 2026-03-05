package io.github.rebeccacalabretta.teamops.domain.repository

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
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

    suspend fun updateVacationStatus(
        requestId: String,
        status: VacationStatus,
        decidedBy: String,
        decidedAt: Long
    )

    suspend fun updateVacation(
        requestId: String,
        startDate: LocalDate,
        endDate: LocalDate
    )

    suspend fun deleteVacation(
        requestId: String
    )
}