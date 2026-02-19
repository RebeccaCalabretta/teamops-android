package io.github.rebeccacalabretta.teamops.domain.menu

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

object RoleMenuConfig {
    val menuItems = listOf(
        MenuItem(
            id = "punch",
            title = "Zeiterfassung",
            allowedRoles = setOf(
                EmployeeRole.WORKER,
                EmployeeRole.MANAGER,
                EmployeeRole.HR,
                EmployeeRole.ADMIN
            )
        ),
        MenuItem(
            id = "employees",
            title = "Mitarbeiter",
            allowedRoles = setOf(
                EmployeeRole.MANAGER,
                EmployeeRole.HR,
                EmployeeRole.ADMIN
            )
        ),
        MenuItem(
            id = "schedule",
            title = "Arbeitsplan",
            allowedRoles = setOf(
                EmployeeRole.WORKER,
                EmployeeRole.MANAGER,
                EmployeeRole.HR,
                EmployeeRole.ADMIN
            )
        ),
        MenuItem(
            id = "vacation",
            title = "Urlaub",
            allowedRoles = setOf(
                EmployeeRole.WORKER,
                EmployeeRole.MANAGER,
                EmployeeRole.HR,
                EmployeeRole.ADMIN
            )
        )
    )



    fun itemsForRole(role: EmployeeRole): List<MenuItem> =
        menuItems.filter { role in it.allowedRoles }
}