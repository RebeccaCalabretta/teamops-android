package io.github.rebeccacalabretta.teamops.data.export

data class ExportSessionRow(
    val employeeName: String,
    val objectName: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val duration: String,
    val distanceMeters: Double? = null
)