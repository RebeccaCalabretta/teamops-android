package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import javax.inject.Inject

class ApproveVacationRequestUseCase @Inject constructor(
    private val repository: VacationRepository
) {
    suspend operator fun invoke(
        requestId: String,
        newStatus: VacationStatus,
        decidedBy: String
    ) {
    }
}