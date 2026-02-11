package io.github.rebeccacalabretta.teamops.ui.model

import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity

fun EmployeeEntity.toEmployeeRowUiModel(
    monthlyWorkText: String
): EmployeeRowUiModel =
    EmployeeRowUiModel(
        id = id,
        name = name,
        role = role,
        monthlyWorkText = monthlyWorkText
    )