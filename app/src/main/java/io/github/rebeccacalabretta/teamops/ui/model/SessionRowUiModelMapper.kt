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

    return SessionRowUiModel(
        id = session.id,
        objectName = obj?.name ?: "Unbekannt",
        startTime = session.startTime,
        endTime = session.endTime,
        isCheckedIn = isCheckedIn,
        isCheckedOutOutsideRadius = isCheckedOutOutsideRadius
    )
}