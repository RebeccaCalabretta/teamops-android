package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees ORDER BY name ASC")
    fun getAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("""
        SELECT * FROM employees
        WHERE role = :role
        ORDER BY name ASC
    """)
    fun getEmployeesByRole(role: EmployeeRole): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEmployees(employees: List<EmployeeEntity>)
}