package io.github.rebeccacalabretta.teamops.navigation

import androidx.navigation.NavHostController
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.canAccessEmployee

fun NavHostController.navigateIfAllowed(
    currentUserId: String,
    currentRole: EmployeeRole,
    targetEmployeeId: String,
    route: Any
) {
    if(
        canAccessEmployee(
            currentUserId = currentUserId,
            currentRole = currentRole,
            targetEmployeeId = targetEmployeeId
        )
    ) {
        navigate(route)
    }
}