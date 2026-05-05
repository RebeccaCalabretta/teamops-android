package io.github.rebeccacalabretta.teamops.data.sample

import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

object SampleEmployees {
    val items: List<EmployeeEntity> = listOf(
        EmployeeEntity(
            id = "emp_001",
            name = "Rainer Zufall",
            role = EmployeeRole.WORKER,
            managerId = "emp_003"
        ),
        EmployeeEntity(
            id = "emp_002",
            name = "Isolde Maduschen",
            role = EmployeeRole.WORKER,
            managerId = "emp_003"
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
        ),
        EmployeeEntity(
            id = "emp_006",
            name = "Demo Worker",
            role = EmployeeRole.WORKER
        )
    )
}