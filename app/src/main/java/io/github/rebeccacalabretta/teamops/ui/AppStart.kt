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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.menu.RoleMenuConfig
import io.github.rebeccacalabretta.teamops.navigation.AppNavHost
import io.github.rebeccacalabretta.teamops.ui.components.DrawerMenu
import kotlinx.coroutines.launch

@Composable
fun AppStart() {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentRole = EmployeeRole.MANAGER

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                items = RoleMenuConfig.itemsForRole(currentRole),
                onItemClick = { item ->
                    navController.navigate(item.id)

                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(0)
        ) { padding ->
            AppNavHost(
                navController = navController,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.padding(padding)
            )
        }
    }
}