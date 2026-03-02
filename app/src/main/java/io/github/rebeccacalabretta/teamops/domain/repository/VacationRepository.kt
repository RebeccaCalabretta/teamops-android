package io.github.rebeccacalabretta.teamops.domain.repository

import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import kotlinx.coroutines.flow.Flow

interface VacationRepository {

    fun observeVacationsForEmployee(
        employeeId: String
    ): Flow<List<VacationEntry>>
}