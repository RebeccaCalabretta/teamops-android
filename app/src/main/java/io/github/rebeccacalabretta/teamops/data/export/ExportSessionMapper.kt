package io.github.rebeccacalabretta.teamops.data.export

import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.util.SessionFormat

fun mapToExportSessionRow(
    session: PunchSessionEntity,
    objectName: String,
    employeeName: String = "-"
): ExportSessionRow = ExportSessionRow(
    employeeName = employeeName,
    objectName = objectName,
    date = SessionFormat.formatDate(session.startTime),
    startTime = SessionFormat.formatTime(session.startTime),
    endTime = SessionFormat.formatTime(session.endTime),
    duration =  SessionFormat.formatDuration(session.startTime, session.endTime),
    distanceMeters = session.checkOutDistanceMeters
)