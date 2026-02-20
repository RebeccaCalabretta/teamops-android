package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.menu.RoleMenuConfig
import io.github.rebeccacalabretta.teamops.navigation.AppNavHost
import io.github.rebeccacalabretta.teamops.navigation.EmployeeRoute
import io.github.rebeccacalabretta.teamops.navigation.PunchRoute
import io.github.rebeccacalabretta.teamops.navigation.ScheduleRoute
import io.github.rebeccacalabretta.teamops.navigation.VacationRoute
import io.github.rebeccacalabretta.teamops.ui.components.AppTopBar
import io.github.rebeccacalabretta.teamops.ui.components.DrawerMenu
import io.github.rebeccacalabretta.teamops.ui.state.TopBarConfig
import kotlinx.coroutines.launch

@Composable
fun AppStart() {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var dynamicTitle by remember { mutableStateOf<String?>(null) }

    /*val currentUserId = "emp_001"
    val currentRole = EmployeeRole.WORKER
    val teamMemberIds: Set<String> = emptySet()*/

    /*val currentUserId = "emp_003"
    val currentRole = EmployeeRole.MANAGER
    val teamMemberIds = setOf("emp_001", "emp_002")*/

    val currentUserId = "emp_004"
    val currentRole = EmployeeRole.HR
    val teamMemberIds: Set<String> = emptySet()

    val topBarConfig = when {
        currentRoute?.contains("PunchRoute") == true ->
            TopBarConfig.Root("Zeiterfassung")

        currentRoute?.contains("EmployeeRoute") == true ->
            TopBarConfig.Root("Mitarbeiter")

        currentRoute?.contains("ScheduleRoute") == true ->
            TopBarConfig.Root("Arbeitsplan")

        currentRoute?.contains("VacationRoute") == true ->
            TopBarConfig.Root("Urlaub")

        currentRoute?.contains("EmployeeSessionRoute") == true ->
            TopBarConfig.Child(dynamicTitle ?: "Mitarbeiter")

        else ->
            TopBarConfig.Root("")
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                items = RoleMenuConfig.itemsForRole(currentRole),
                onItemClick = { item ->
                    val targetRoute = when (item.id) {
                        "punch" -> PunchRoute
                        "employees" -> EmployeeRoute
                        "schedule" -> ScheduleRoute
                        "vacation" -> VacationRoute
                        else -> null
                    }

                    targetRoute?.let { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = false

                            popUpTo(0) {
                                inclusive = false
                            }
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
                    config = topBarConfig,
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
                currentUserId = currentUserId,
                currentRole = currentRole,
                teamMemberIds = teamMemberIds,
                onSetTopBarTitle = { title ->
                    dynamicTitle = title
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}