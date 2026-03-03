package io.github.rebeccacalabretta.teamops.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.ui.employee.EmployeeScreen
import io.github.rebeccacalabretta.teamops.ui.employeeSession.EmployeeSessionContainer
import io.github.rebeccacalabretta.teamops.ui.punch.PunchContainer
import io.github.rebeccacalabretta.teamops.ui.schedule.ScheduleScreen
import io.github.rebeccacalabretta.teamops.ui.vacation.VacationScreen
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeVisibility

@Composable
fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    currentUserId: String,
    currentRole: EmployeeRole,
    teamMemberIds: Set<String>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScheduleRoute(currentUserId),
        modifier = modifier
    ) {

        composable<PunchRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PunchRoute>()

            PunchContainer(
                employeeId = route.employeeId,
                currentUserId = currentUserId,
                snackbarHostState = snackbarHostState
            )
        }

        composable<ScheduleRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ScheduleRoute>()

            ScheduleScreen(
                employeeId = route.employeeId,
                currentUserId = currentUserId,
                currentRole = currentRole,
                teamMemberIds = teamMemberIds
            )
        }

        composable<VacationRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<VacationRoute>()

            VacationScreen(
                employeeId = route.employeeId,
                currentUserId = currentUserId,
                currentRole = currentRole,
                teamMemberIds = teamMemberIds
            )
        }

        composable<EmployeeRoute> {

            if (currentRole == EmployeeRole.WORKER) {
                navController.popBackStack()
                return@composable
            }

            val viewModel: EmployeeViewModel = hiltViewModel()

            LaunchedEffect(currentUserId, currentRole, teamMemberIds) {
                viewModel.setVisibility(
                    EmployeeVisibility(
                        currentUserId = currentUserId,
                        currentRole = currentRole,
                        teamMemberIds = teamMemberIds
                    )
                )
            }

            EmployeeScreen(
                onEmployeeClick = { employeeId ->
                    navController.navigateIfAllowed(
                        currentUserId = currentUserId,
                        currentRole = currentRole,
                        targetEmployeeId = employeeId,
                        teamMemberIds = teamMemberIds
                    ) {
                        navController.navigate(EmployeeSessionRoute(employeeId))
                    }
                },
                onScheduleClick = { employeeId ->
                    navController.navigateIfAllowed(
                        currentUserId = currentUserId,
                        currentRole = currentRole,
                        targetEmployeeId = employeeId,
                        teamMemberIds = teamMemberIds
                    ) {
                        navController.navigate(ScheduleRoute(employeeId))
                    }
                },
                onVacationClick = { employeeId ->
                    navController.navigateIfAllowed(
                        currentUserId = currentUserId,
                        currentRole = currentRole,
                        targetEmployeeId = employeeId,
                        teamMemberIds = teamMemberIds
                    ) {
                        navController.navigate(VacationRoute(employeeId))
                    }
                }
            )
        }

        composable<EmployeeSessionRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<EmployeeSessionRoute>()

            EmployeeSessionContainer(
                employeeId = route.employeeId,
                currentUserId = currentUserId
            )
        }
    }
}