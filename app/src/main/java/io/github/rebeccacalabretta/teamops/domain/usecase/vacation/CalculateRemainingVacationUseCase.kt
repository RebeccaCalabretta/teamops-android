package io.github.rebeccacalabretta.teamops.domain.usecase.vacation

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationBalance
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import javax.inject.Inject

class CalculateRemainingVacationUseCase @Inject constructor() {

    operator fun invoke(
        vacations: List<VacationEntry>,
        year: Int
    ): VacationBalance {
        return VacationBalance(
            usedDays = 0,
            remainingDays = 0
        )
    }
}