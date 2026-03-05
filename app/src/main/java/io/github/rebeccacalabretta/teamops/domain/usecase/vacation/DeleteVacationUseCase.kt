package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.error.PermissionDeniedException
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import javax.inject.Inject

class DeleteVacationUseCase @Inject constructor(
    private val repository: VacationRepository
) {
    suspend operator fun invoke(
        requestId: String,
        currentUserId: String,
        currentRole: EmployeeRole
    ) {
        if (currentRole != EmployeeRole.HR &&
            currentRole != EmployeeRole.ADMIN
        ) {
            throw PermissionDeniedException()
        }
        repository.deleteVacation(requestId)
    }
}