package io.github.rebeccacalabretta.teamops.ui.model

import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity

fun mapToSessionUiModel(
    session: PunchSessionEntity,
    obj: ObjectEntity?
): SessionRowUiModel {

    val isCheckedIn = session.endTime == null

    val isCheckedOutOutsideRadius =
        obj != null &&
                session.checkOutDistanceMeters != null &&
                session.checkOutDistanceMeters > obj.radiusMeters.toDouble()

    val end = session.endTime ?: System.currentTimeMillis()
    val duration = (end - session.startTime).coerceAtLeast(0L)

    val zone = java.time.ZoneId.systemDefault()
    val yearMonth = java.time.YearMonth.from(
        java.time.Instant.ofEpochMilli(session.startTime)
            .atZone(zone)
    )

    return SessionRowUiModel(
        id = session.id,
        objectName = obj?.name ?: "Unbekannt",
        startTime = session.startTime,
        endTime = session.endTime,
        isCheckedIn = isCheckedIn,
        isCheckedOutOutsideRadius = isCheckedOutOutsideRadius,
        durationMillis = duration,
        yearMonth = yearMonth
    )
}