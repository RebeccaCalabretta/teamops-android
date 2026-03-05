package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.error.PermissionDeniedException
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdateVacationUseCase @Inject constructor(
    private val repository: VacationRepository
) {
    suspend operator fun invoke(
        requestId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        currentUserId: String,
        currentRole: EmployeeRole
    ) {
        if (currentRole != EmployeeRole.HR &&
            currentRole != EmployeeRole.ADMIN
        ) {
            throw PermissionDeniedException()
        }
        repository.updateVacation(
            requestId = requestId,
            startDate = startDate,
            endDate = endDate
        )
    }
}