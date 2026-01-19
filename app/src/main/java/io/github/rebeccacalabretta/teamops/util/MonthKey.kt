package io.github.rebeccacalabretta.teamops.util

import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object MonthKey {
    private val keyFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM")

    private val displayFormatter =
        DateTimeFormatter.ofPattern("MMMM yyyy")

    fun fromTimestamp(timestamp: Long): String =
        YearMonth.from(
            Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
        ).format(keyFormatter)

    fun toDisplay(monthKey: String): String =
        YearMonth.parse(monthKey, keyFormatter)
            .format(displayFormatter.withLocale(Locale.getDefault()))
}