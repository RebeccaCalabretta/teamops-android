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

    val currentRole = EmployeeRole.MANAGER

    val topBarConfig = when {
        currentRoute == "punch" ->
            TopBarConfig.Root("Zeiterfassung")

        currentRoute == "employees" ->
            TopBarConfig.Root("Mitarbeiter")

        currentRoute == "schedule" ->
            TopBarConfig.Root("Arbeitsplan")

        currentRoute == "vacation" ->
            TopBarConfig.Root("Urlaub")

        currentRoute?.startsWith("employeeSession/") == true ->
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
                        "punch" -> "punch"
                        "employees" -> "employees"
                        "schedule" -> "schedule"
                        "vacation" -> "vacation"
                        else -> null
                    }

                    targetRoute?.let { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
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
                onSetTopBarTitle = { title ->
                    dynamicTitle = title
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}