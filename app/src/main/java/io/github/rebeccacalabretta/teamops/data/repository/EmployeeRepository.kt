package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    fun getEmployees(): Flow<List<EmployeeEntity>>

    fun getEmployeesByRole(role: EmployeeRole): Flow<List<EmployeeEntity>>

    suspend fun upsertEmployee(employee: EmployeeEntity)

    suspend fun seedIfEmpty()
}