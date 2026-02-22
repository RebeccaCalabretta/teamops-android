package io.github.rebeccacalabretta.teamops.navigation

import kotlinx.serialization.Serializable

@Serializable
data class PunchRoute(
    val employeeId: String
)

@Serializable
data object EmployeeRoute

@Serializable
data class ScheduleRoute(
    val employeeId: String
)

@Serializable
data class VacationRoute(
    val employeeId: String
)

@Serializable
data class EmployeeSessionRoute(
    val employeeId: String
)