package io.github.rebeccacalabretta.teamops.navigation

import kotlinx.serialization.Serializable

@Serializable
object PunchRoute

@Serializable
object EmployeeRoute

@Serializable
object ScheduleRoute

@Serializable
data class EmployeeSessionRoute(
    val employeeId: String
)