package io.github.rebeccacalabretta.teamops.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.rebeccacalabretta.teamops.ui.employee.EmployeeScreen
import io.github.rebeccacalabretta.teamops.ui.employeeSession.EmployeeSessionScreen
import io.github.rebeccacalabretta.teamops.ui.punch.PunchContainer

@Composable
fun AppNavHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

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
                    navController.navigate(EmployeeSessionRoute(employeeId))
                }
            )
        }

        composable<EmployeeSessionRoute> { entry ->
            val route = entry.toRoute<EmployeeSessionRoute>()
            EmployeeSessionScreen(
                employeeId = route.employeeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}