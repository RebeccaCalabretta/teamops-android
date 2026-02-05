package io.github.rebeccacalabretta.teamops.util

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.math.max

object SessionFormat {

    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.")
            .withLocale(Locale.getDefault())

    private val timeFormatter =
        DateTimeFormatter.ofPattern("HH:mm")
            .withLocale(Locale.getDefault())

    fun formatDate(timestamp: Long): String =
        Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)

    fun formatTime(timestamp: Long?): String =
        if (timestamp == null) "-"
        else
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

    fun parseTimeToMillis(
        dateMillis: Long,
        timeText: String
    ): Long {
        val localTime = try {
            LocalTime.parse(timeText.trim(), timeFormatter)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid time format: $timeText")
        }

        val zone = ZoneId.systemDefault()

        return Instant.ofEpochMilli(dateMillis)
            .atZone(zone)
            .toLocalDate()
            .atTime(localTime)
            .atZone(zone)
            .toInstant()
            .toEpochMilli()
    }
}