package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.remote.VacationDataSource
import io.github.rebeccacalabretta.teamops.data.remote.VacationDocument
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class FirebaseVacationRepository @Inject constructor(
    private val dataSource: VacationDataSource
) : VacationRepository {

    override fun observeVacationsForEmployee(
        employeeId: String
    ): Flow<List<VacationEntry>> =
        dataSource
            .observeVacationsForEmployee(employeeId)
            .map { documents ->
                documents.map { it.toDomain() }
            }

    private fun VacationDocument.toDomain(): VacationEntry =
        VacationEntry(
            id = id,
            employeeId = employeeId,
            startDate = startDate.toLocalDate(),
            endDate = endDate.toLocalDate(),
            status = status.toVacationStatus()
        )

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

    private fun String.toVacationStatus(): VacationStatus =
        VacationStatus.valueOf(this)
}