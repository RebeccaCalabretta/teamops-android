package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationBalance
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

private const val DEFAULT_YEARLY_VACATION_DAYS = 30

class CalculateRemainingVacationUseCase @Inject constructor() {

    operator fun invoke(
        vacations: List<VacationEntry>,
        year: Int = LocalDate.now().year
    ): VacationBalance {

        val usedDays = vacations
            .filter { it.status == VacationStatus.APPROVED }
            .filter { it.startDate.year == year || it.endDate.year == year }
            .sumOf {
                ChronoUnit.DAYS
                    .between(it.startDate, it.endDate)
                    .toInt() + 1
            }

        val remaining = (DEFAULT_YEARLY_VACATION_DAYS - usedDays)
            .coerceAtLeast(0)

        return VacationBalance(
            yearlyTotal = DEFAULT_YEARLY_VACATION_DAYS,
            usedDays = usedDays,
            remainingDays = remaining
        )
    }
}