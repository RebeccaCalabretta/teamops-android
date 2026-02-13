package io.github.rebeccacalabretta.teamops.domain.access

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

fun canAccessEmployee(
    currentUserId: String,
    currentRole: EmployeeRole,
    targetEmployeeId: String,
    managedEmployeeIds: Set<String> = emptySet()
): Boolean {
    return when (currentRole) {
        EmployeeRole.WORKER ->
            currentUserId == targetEmployeeId

        EmployeeRole.MANAGER ->
            currentUserId == targetEmployeeId ||
                    managedEmployeeIds.contains(targetEmployeeId)

        EmployeeRole.HR,
        EmployeeRole.ADMIN ->
            true
    }
}