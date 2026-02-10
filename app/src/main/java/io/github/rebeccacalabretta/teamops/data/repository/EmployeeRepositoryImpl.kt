package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.EmployeeDao
import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {

    override fun getEmployees(): Flow<List<EmployeeEntity>> =
        employeeDao.getAllEmployees()

}