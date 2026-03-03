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
import java.time.temporal.ChronoUnit
import java.util.UUID
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

    override suspend fun submitVacationRequest(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        requestedBy: String
    ) {
        val nowMillis = System.currentTimeMillis()
        val daysRequested = (ChronoUnit.DAYS.between(startDate, endDate) + 1)
            .toInt()
            .coerceAtLeast(1)

        val document = VacationDocument(
            id = UUID.randomUUID().toString(),
            employeeId = employeeId,
            startDate = startDate.toStartOfDayMillis(),
            endDate = endDate.toStartOfDayMillis(),
            daysRequested = daysRequested,
            status = VacationStatus.REQUESTED.name,
            createdAt = nowMillis,
            decidedAt = null,
            decidedBy = null
        )

        dataSource.upsert(document)
    }

    override suspend fun updateVacationStatus(
        requestId: String,
        status: VacationStatus,
        decidedBy: String,
        decidedAt: Long
    ) {
        dataSource.updateStatus(
            requestId = requestId,
            status = status.name,
            decidedBy = decidedBy,
            decidedAt = decidedAt
        )
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

    private fun LocalDate.toStartOfDayMillis(): Long =
        atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    private fun String.toVacationStatus(): VacationStatus =
        runCatching { VacationStatus.valueOf(this) }
            .getOrDefault(VacationStatus.REQUESTED)
}