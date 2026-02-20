package io.github.rebeccacalabretta.teamops.navigation

import androidx.navigation.NavHostController
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.access.canAccessEmployee

fun NavHostController.navigateIfAllowed(
    currentUserId: String,
    currentRole: EmployeeRole,
    targetEmployeeId: String,
    teamMemberIds: Set<String> = emptySet(),
    onAllowed: () -> Unit
) {
    if (
        canAccessEmployee(
            currentUserId = currentUserId,
            currentRole = currentRole,
            targetEmployeeId = targetEmployeeId,
            teamMemberIds = teamMemberIds
        )
    ) {
        onAllowed()
    }
}