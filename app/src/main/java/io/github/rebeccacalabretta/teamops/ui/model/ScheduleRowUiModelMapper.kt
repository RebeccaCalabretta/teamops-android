package io.github.rebeccacalabretta.teamops.ui.model

import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import java.time.Instant
import java.time.ZoneId

fun ScheduleEntity.toScheduleRowUiModel(
    objectName: String,
    canEdit: Boolean
): ScheduleRowUiModel =
    ScheduleRowUiModel(
        id = id,
        employeeId = employeeId,
        objectId = objectId,
        objectName = objectName,
        date = Instant.ofEpochMilli(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        startTime = Instant.ofEpochMilli(startTime)
            .atZone(ZoneId.systemDefault())
            .toLocalTime(),
        endTime = Instant.ofEpochMilli(endTime)
            .atZone(ZoneId.systemDefault())
            .toLocalTime(),
        canEdit = canEdit
    )