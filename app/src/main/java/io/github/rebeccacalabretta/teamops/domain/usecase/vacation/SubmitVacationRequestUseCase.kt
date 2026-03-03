package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import java.time.LocalDate
import javax.inject.Inject

class SubmitVacationRequestUseCase @Inject constructor(
    private val repository: VacationRepository
) {
    suspend operator fun invoke(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        requestedBy: String
    ) {
        repository.submitVacationRequest(
            employeeId = employeeId,
            startDate = startDate,
            endDate = endDate,
            requestedBy = requestedBy
        )
    }
}