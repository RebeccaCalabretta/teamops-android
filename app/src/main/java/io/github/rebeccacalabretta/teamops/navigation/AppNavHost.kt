package io.github.rebeccacalabretta.teamops.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.rebeccacalabretta.teamops.ui.employee.EmployeeScreen
import io.github.rebeccacalabretta.teamops.ui.employeeSession.EmployeeSessionScreen
import io.github.rebeccacalabretta.teamops.ui.punch.PunchContainer
import io.github.rebeccacalabretta.teamops.ui.schedule.ScheduleScreen
import io.github.rebeccacalabretta.teamops.ui.vacation.VacationScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onSetTopBarTitle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "schedule",
        modifier = modifier
    ) {
        composable("punch") {
            PunchContainer(snackbarHostState = snackbarHostState)
        }

        composable("schedule") {
            ScheduleScreen()
        }

        composable("vacation") {
            VacationScreen()
        }

        composable("employees") {
            EmployeeScreen(
                onEmployeeClick = { employeeId ->
                    navController.navigate("employeeSession/$employeeId")
                }
            )
        }

        composable("employeeSession/{employeeId}") { entry ->
            val employeeId = entry.arguments?.getString("employeeId") ?: return@composable
            EmployeeSessionScreen(
                employeeId = employeeId,
                setTopBarTitle = onSetTopBarTitle
            )
        }
    }
}