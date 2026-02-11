package io.github.rebeccacalabretta.teamops.ui.model

import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

data class EmployeeRowUiModel(
    val id: String,
    val name: String,
    val role: EmployeeRole,
    val monthlyWorkText: String
)