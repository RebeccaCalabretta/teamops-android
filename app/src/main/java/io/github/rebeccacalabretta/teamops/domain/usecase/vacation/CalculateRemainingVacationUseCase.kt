package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationBalance
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

private const val DEFAULT_YEARLY_VACATION_DAYS = 30

class CalculateRemainingVacationUseCase @Inject constructor() {

    operator fun invoke(
        vacations: List<VacationEntry>,
        year: Int = LocalDate.now().year
    ): VacationBalance {

        val usedDays =
            vacations
                .filter { it.status == VacationStatus.APPROVED }
                .sumOf { vacation ->
                    countWorkingDaysInYear(
                        startDate = vacation.startDate,
                        endDate = vacation.endDate,
                        year = year
                    )
                }

        val remainingDays =
            (DEFAULT_YEARLY_VACATION_DAYS - usedDays)
                .coerceAtLeast(0)

        return VacationBalance(
            yearlyTotal = DEFAULT_YEARLY_VACATION_DAYS,
            usedDays = usedDays,
            remainingDays = remainingDays
        )
    }

    private fun countWorkingDaysInYear(
        startDate: LocalDate,
        endDate: LocalDate,
        year: Int
    ): Int {
        val yearStart = LocalDate.of(year, 1, 1)
        val yearEnd = LocalDate.of(year, 12, 31)

        val effectiveStart = maxOf(startDate, yearStart)
        val effectiveEnd = minOf(endDate, yearEnd)

        if (effectiveStart.isAfter(effectiveEnd)) return 0

        return generateSequence(effectiveStart) { date ->
            date.plusDays(1).takeIf { !it.isAfter(effectiveEnd) }
        }.count { date ->
            date.dayOfWeek != DayOfWeek.SATURDAY &&
                    date.dayOfWeek != DayOfWeek.SUNDAY
        }
    }
}