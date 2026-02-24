package io.github.rebeccacalabretta.teamops.domain.model

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

data class UserSession(
    val employeeId: String,
    val role: EmployeeRole,
    val teamMemberIds: Set<String> = emptySet()
)