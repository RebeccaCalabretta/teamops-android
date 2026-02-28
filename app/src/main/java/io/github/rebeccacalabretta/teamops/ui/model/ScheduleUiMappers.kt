package io.github.rebeccacalabretta.teamops.ui.model

import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import java.time.ZoneId

fun ScheduleRowUiModel.toScheduleEntity(
    employeeId: String,
    createdBy: String
): ScheduleEntity {

    val zone = ZoneId.systemDefault()

    return ScheduleEntity(
        id = id,
        employeeId = employeeId,
        objectId = objectId,
        date = date
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli(),
        startTime = date
            .atTime(startTime)
            .atZone(zone)
            .toInstant()
            .toEpochMilli(),
        endTime = date
            .atTime(endTime)
            .atZone(zone)
            .toInstant()
            .toEpochMilli(),
        createdBy = createdBy
    )
}