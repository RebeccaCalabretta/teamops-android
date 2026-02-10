package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor() : EmployeeRepository {

    override fun getEmployees(): Flow<List<EmployeeEntity>> =
        flowOf(
            listOf(
                EmployeeEntity(
                    id = "emp_001",
                    name = "Rainer Zufall",
                    role = EmployeeRole.WORKER
                ),
                EmployeeEntity(
                    id = "emp_002",
                    name = "Isolde Maduschen",
                    role = EmployeeRole.WORKER
                ),
                EmployeeEntity(
                    id = "emp_003",
                    name = "Alda Audi",
                    role = EmployeeRole.MANAGER
                ),
                EmployeeEntity(
                    id = "emp_004",
                    name = "Klara Fall",
                    role = EmployeeRole.HR
                ),
                EmployeeEntity(
                    id = "emp_005",
                    name = "Hella Wahnsinn",
                    role = EmployeeRole.ADMIN
                )
            )
        )
}