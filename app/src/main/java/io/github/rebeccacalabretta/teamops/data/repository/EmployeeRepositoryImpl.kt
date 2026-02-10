package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.EmployeeDao
import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import io.github.rebeccacalabretta.teamops.data.sample.SampleEmployees
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {

    override fun getEmployees(): Flow<List<EmployeeEntity>> =
        employeeDao.getAllEmployees()

    override suspend fun seedIfEmpty() {
        val existing = employeeDao.getAllEmployees().first()
        if (existing.isEmpty()) {
            employeeDao.upsertEmployees(SampleEmployees.items)
        }
    }
}