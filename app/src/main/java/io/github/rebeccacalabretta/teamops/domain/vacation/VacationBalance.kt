package io.github.rebeccacalabretta.teamops.domain.vacation

data class VacationBalance(
    val yearlyTotal: Int,
    val usedDays: Int,
    val remainingDays: Int
)