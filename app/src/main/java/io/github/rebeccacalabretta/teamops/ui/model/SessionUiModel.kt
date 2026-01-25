package io.github.rebeccacalabretta.teamops.ui.model

data class SessionUiModel(
    val id: String,
    val objectName: String,
    val startTime: Long,
    val endTime: Long?,
    val isCheckedIn: Boolean,
    val isCheckedOutOutsideRadius: Boolean
)