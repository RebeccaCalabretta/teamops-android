package io.github.rebeccacalabretta.teamops.ui.model

import java.time.YearMonth

data class SessionRowUiModel(
    val id: String,
    val objectName: String,
    val startTime: Long,
    val endTime: Long?,
    val isCheckedIn: Boolean,
    val isCheckedOutOutsideRadius: Boolean,
    val durationMillis: Long,
    val yearMonth: YearMonth
)