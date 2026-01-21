package io.github.rebeccacalabretta.teamops.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max

object SessionFormat {

    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yyyy")
            .withLocale(Locale.getDefault())

    private val timeFormatter =
        DateTimeFormatter.ofPattern("HH:mm")
            .withLocale(Locale.getDefault())

    fun formatDate(timestamp: Long): String =
        Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)

    fun formatTime(timestamp: Long): String =
        Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(timeFormatter)

    fun formatDuration(startTime: Long, endTime: Long?): String {
        if (endTime == null) return "--"

        val durationMillis = endTime - startTime
        val totalMinutes = max(0, durationMillis / 60_000L)

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return if (hours > 0) "$hours h $minutes m" else "$minutes m"
    }
}