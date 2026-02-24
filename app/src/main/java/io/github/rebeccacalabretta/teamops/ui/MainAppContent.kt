package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.domain.menu.RoleMenuConfig
import io.github.rebeccacalabretta.teamops.navigation.AppNavHost
import io.github.rebeccacalabretta.teamops.navigation.EmployeeRoute
import io.github.rebeccacalabretta.teamops.navigation.PunchRoute
import io.github.rebeccacalabretta.teamops.navigation.ScheduleRoute
import io.github.rebeccacalabretta.teamops.navigation.VacationRoute
import io.github.rebeccacalabretta.teamops.ui.components.AppTopBar
import io.github.rebeccacalabretta.teamops.ui.components.DrawerMenu
import io.github.rebeccacalabretta.teamops.viewmodel.UserSessionViewModel
import kotlinx.coroutines.launch

@Composable
fun MainAppContent(
    viewModel: UserSessionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    val session = state.session ?: return

    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val title = when {
        currentRoute?.contains("PunchRoute") == true ->
            stringResource(R.string.title_punch)

        currentRoute?.contains("EmployeeRoute") == true ->
            stringResource(R.string.title_employees)

        currentRoute?.contains("ScheduleRoute") == true ->
            stringResource(R.string.title_schedule)

        currentRoute?.contains("VacationRoute") == true ->
            stringResource(R.string.title_vacation)

        currentRoute?.contains("EmployeeSessionRoute") == true ->
            stringResource(R.string.title_punch)

        else -> ""
    }

    val routeEmployeeId = backStackEntry
        ?.arguments
        ?.getString("employeeId")

    val showBackButton =
        routeEmployeeId != null && routeEmployeeId != session.employeeId

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                items = RoleMenuConfig.itemsForRole(session.role),
                onItemClick = { item ->
                    val targetRoute = when (item.id) {
                        "punch" -> PunchRoute(session.employeeId)
                        "employees" -> EmployeeRoute
                        "schedule" -> ScheduleRoute(session.employeeId)
                        "vacation" -> VacationRoute(session.employeeId)
                        else -> null
                    }

                    targetRoute?.let { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = false
                            popUpTo(0) { inclusive = false }
                        }
                    }

                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = title,
                    showBackButton = showBackButton,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = { navController.popBackStack() }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(0)
        ) { padding ->
            AppNavHost(
                navController = navController,
                snackbarHostState = snackbarHostState,
                currentUserId = session.employeeId,
                currentRole = session.role,
                teamMemberIds = session.teamMemberIds,
                modifier = Modifier.padding(padding)
            )
        }
    }
}