package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
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
        currentUserId: String,
        currentRole: EmployeeRole,
        teamMemberIds: Set<String>
    ) {
        val isSelfRequest = employeeId == currentUserId

        val allowed = when {
            isSelfRequest -> true

            currentRole == EmployeeRole.MANAGER &&
                    employeeId in teamMemberIds -> true

            currentRole == EmployeeRole.HR ||
                    currentRole == EmployeeRole.ADMIN -> true

            else -> false
        }

        if(!allowed) {
            throw IllegalStateException("Not allowed to submit vacation request")
        }

        repository.submitVacationRequest(
            employeeId = employeeId,
            startDate = startDate,
            endDate = endDate,
            requestedBy = currentUserId
        )
    }
}