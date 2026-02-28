package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.ScheduleDao
import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import io.github.rebeccacalabretta.teamops.data.remote.ScheduleDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val remote: ScheduleDataSource
) : ScheduleRepository {
    override fun getScheduleForEmployee(employeeId: String): Flow<List<ScheduleEntity>> =
        scheduleDao.getScheduleForEmployee(employeeId)

    override suspend fun upsertSchedule(entry: ScheduleEntity) =
        scheduleDao.upsertSchedule(entry)

    override suspend fun deleteSchedule(entry: ScheduleEntity) =
        scheduleDao.deleteSchedule(entry.id)
}