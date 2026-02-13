package io.github.rebeccacalabretta.teamops.domain.menu

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

data class MenuItem(
    val id: String,
    val title: String,
    val allowedRoles: Set<EmployeeRole>
)