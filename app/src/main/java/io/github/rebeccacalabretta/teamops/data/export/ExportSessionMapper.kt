package io.github.rebeccacalabretta.teamops.data.export

import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.util.time.DateTimeFormat

fun mapToExportSessionRow(
    session: PunchSessionEntity,
    objectName: String,
    employeeName: String = "-"
): ExportSessionRow = ExportSessionRow(
    employeeName = employeeName,
    objectName = objectName,
    date = DateTimeFormat.formatDate(session.startTime),
    startTime = DateTimeFormat.formatTime(session.startTime),
    endTime = DateTimeFormat.formatTime(session.endTime),
    duration =  DateTimeFormat.formatDuration(session.startTime, session.endTime),
    distanceMeters = session.checkOutDistanceMeters
)