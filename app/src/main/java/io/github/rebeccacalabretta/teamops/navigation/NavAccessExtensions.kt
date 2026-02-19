package io.github.rebeccacalabretta.teamops.navigation

import androidx.navigation.NavHostController
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.access.canAccessEmployee

fun NavHostController.navigateIfAllowed(
    currentUserId: String,
    currentRole: EmployeeRole,
    targetEmployeeId: String,
    route: String,
    teamMemberIds: Set<String> = emptySet()
) {
    if(
        canAccessEmployee(
            currentUserId = currentUserId,
            currentRole = currentRole,
            targetEmployeeId = targetEmployeeId,
            teamMemberIds = teamMemberIds
        )
    ) {
        navigate(route)
    }
}