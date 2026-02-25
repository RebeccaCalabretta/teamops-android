package io.github.rebeccacalabretta.teamops.ui

import androidx.navigation.NavHostController
import io.github.rebeccacalabretta.teamops.navigation.EmployeeRoute
import io.github.rebeccacalabretta.teamops.navigation.PunchRoute
import io.github.rebeccacalabretta.teamops.navigation.ScheduleRoute
import io.github.rebeccacalabretta.teamops.navigation.VacationRoute

private const val MENU_PUNCH = "punch"
private const val MENU_EMPLOYEES = "employees"
private const val MENU_SCHEDULE = "schedule"
private const val MENU_VACATION = "vacation"
private const val MENU_LOGOUT = "logout"

fun NavHostController.onDrawerMenuClick(
    itemId: String,
    sessionEmployeeId: String,
    onLogout: () -> Unit
) {
    when (itemId) {
        MENU_PUNCH -> navigate(PunchRoute(sessionEmployeeId)) { launchSingleTop = true }
        MENU_EMPLOYEES -> navigate(EmployeeRoute) { launchSingleTop = true }
        MENU_SCHEDULE -> navigate(ScheduleRoute(sessionEmployeeId)) { launchSingleTop = true }
        MENU_VACATION -> navigate(VacationRoute(sessionEmployeeId)) { launchSingleTop = true }
        MENU_LOGOUT -> onLogout()
    }
}