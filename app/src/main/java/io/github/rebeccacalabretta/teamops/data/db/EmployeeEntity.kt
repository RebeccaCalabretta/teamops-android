package io.github.rebeccacalabretta.teamops.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: EmployeeRole = EmployeeRole.WORKER,
    val managerId: String? = null
)