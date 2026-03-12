package io.github.rebeccacalabretta.teamops.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Suppress("ConstantLocale")
object DateTimeFormat {

    private val zone = ZoneId.systemDefault()

    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.", Locale.getDefault())

    private val timeFormatter =
        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    private val fullDateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault())

    fun formatFullDate(date: LocalDate): String =
        date.format(fullDateFormatter)

    fun formatDate(timestamp: Long): String =
        Instant.ofEpochMilli(timestamp)
            .atZone(zone)
            .format(dateFormatter)

    fun formatDate(date: LocalDate): String =
        date.format(dateFormatter)

    fun formatTime(timestamp: Long?): String =
        timestamp?.let {
            Instant.ofEpochMilli(it)
                .atZone(zone)
                .format(timeFormatter)
        } ?: "-"

    fun formatTime(time: LocalTime): String =
        time.format(timeFormatter)

    fun formatDuration(startTime: Long, endTime: Long?): String =
        if (endTime == null) "--"
        else formatDurationMillis(endTime - startTime)

    fun formatDurationMillis(millis: Long): String {
        val totalMinutes = (millis / 60_000L).coerceAtLeast(0)
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
        } catch (_: DateTimeParseException) {
            throw IllegalArgumentException("Invalid time format: $timeText")
        }

        return Instant.ofEpochMilli(dateMillis)
            .atZone(zone)
            .toLocalDate()
            .atTime(localTime)
            .atZone(zone)
            .toInstant()
            .toEpochMilli()
    }

    fun firstDayOfNextMonthMillis(): Long {
        val now = Instant.now()
            .atZone(zone)
            .toLocalDate()

        val firstOfNextMonth =
            now.plusMonths(1).withDayOfMonth(1)

        return firstOfNextMonth
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
    }
}