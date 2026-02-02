package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor() : EmployeeRepository {
    override fun getEmployees(): Flow<List<EmployeeEntity>> =
        flowOf(
            listOf(
                EmployeeEntity("emp_001", "Rainer Zufall", "worker"),
                EmployeeEntity("emp_002", "Isolde Maduschen", "worker"),
                EmployeeEntity("emp_003", "Alda Audi", "objectmanager"),
                EmployeeEntity("emp_004", "Klara Fall", "office"),
                EmployeeEntity("emp_005", "Hella Wahnsinn", "admin")
            )
        )
}