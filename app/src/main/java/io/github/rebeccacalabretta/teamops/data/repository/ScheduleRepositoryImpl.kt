package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.ScheduleDao
import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import io.github.rebeccacalabretta.teamops.data.remote.ScheduleDataSource
import io.github.rebeccacalabretta.teamops.data.remote.ScheduleDocument
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val remote: ScheduleDataSource
) : ScheduleRepository {

    private fun ScheduleEntity.toDocument(
        lastEditedBy: String? = null,
        lastEditedAt: Long? = null
    ): ScheduleDocument =
        ScheduleDocument(
            id = id,
            employeeId = employeeId,
            objectId = objectId,
            date = date,
            startTime = startTime,
            endTime = endTime,
            createdBy = createdBy,
            lastEditedBy = lastEditedBy,
            lastEditedAt = lastEditedAt
        )

    override fun getScheduleForEmployee(employeeId: String): Flow<List<ScheduleEntity>> =
        scheduleDao.getScheduleForEmployee(employeeId)

    override suspend fun upsertSchedule(
        entry: ScheduleEntity,
        currentUserId: String
    ) {
        val now = System.currentTimeMillis()

        try {
            remote.upsert(
                entry.toDocument(
                    lastEditedBy = currentUserId,
                    lastEditedAt = now
                )
            )
        } catch (e: Exception) {
            throw IllegalStateException("Remote schedule upsert failed", e)
        }

        scheduleDao.upsertSchedule(entry)
    }

    override suspend fun deleteSchedule(
        entry: ScheduleEntity,
        currentUserId: String
    ) {
        try {
            remote.delete(entry.id)
        } catch (e: Exception) {
            throw IllegalStateException("Remote schedule delete failed", e)
        }
        scheduleDao.deleteSchedule(entry.id)

    }
}