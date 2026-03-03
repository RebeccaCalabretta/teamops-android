package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import javax.inject.Inject

class ApproveVacationRequestUseCase @Inject constructor(
    private val repository: VacationRepository
) {
    suspend operator fun invoke(
        requestId: String,
        currentStatus: VacationStatus,
        newStatus: VacationStatus,
        currentUserId: String,
        currentRole: EmployeeRole
    ) {
        if (currentRole != EmployeeRole.HR &&
            currentRole != EmployeeRole.ADMIN
        ) {
            throw IllegalStateException("User is not allowed to approve vacation requests")
        }

        if (currentStatus != VacationStatus.REQUESTED) {
            throw IllegalStateException("Vacation request is not in REQUESTED state")
        }

        val isValidTargetStatus =
            newStatus == VacationStatus.APPROVED ||
                    newStatus == VacationStatus.REJECTED

        if (!isValidTargetStatus) {
            throw IllegalStateException("Invalid status transition")
        }

        val decidedAt = System.currentTimeMillis()

        repository.updateVacationStatus(
            requestId = requestId,
            status = newStatus,
            decidedBy = currentUserId,
            decidedAt = decidedAt
        )
    }
}