package io.github.rebeccacalabretta.teamops.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.access.canAccessEmployee
import io.github.rebeccacalabretta.teamops.ui.employee.EmployeeScreen
import io.github.rebeccacalabretta.teamops.ui.employeeSession.EmployeeSessionScreen
import io.github.rebeccacalabretta.teamops.ui.punch.PunchContainer

@Composable
fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = EmployeeRoute,
        modifier = modifier
    ) {
        composable<PunchRoute> {
            PunchContainer(
                snackbarHostState = snackbarHostState
            )
        }

        composable<EmployeeRoute> {
            EmployeeScreen(
                onEmployeeClick = { employeeId ->

                    val currentUserId = "emp_003"
                    val currentRole = EmployeeRole.MANAGER
                    val managedEmployeeIds = setOf("emp_001", "emp_002")
                    // TODO replace with UserSessionState

                    if (
                        canAccessEmployee(
                            currentUserId = currentUserId,
                            currentRole = currentRole,
                            targetEmployeeId = employeeId,
                            managedEmployeeIds = managedEmployeeIds
                        )
                    ) {
                        navController.navigate(EmployeeSessionRoute(employeeId))
                    }
                }
            )
        }

        composable<EmployeeSessionRoute> { entry ->
            val route = entry.toRoute<EmployeeSessionRoute>()
            EmployeeSessionScreen(
                employeeId = route.employeeId,
                onBackClick = navController::popBackStack
            )
        }
    }
}