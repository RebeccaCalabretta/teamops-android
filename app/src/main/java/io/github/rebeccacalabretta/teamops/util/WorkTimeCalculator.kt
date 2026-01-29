package io.github.rebeccacalabretta.teamops.util

import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object WorkTimeCalculator {

    fun monthWorkMillis(
        sessions: List<PunchSessionEntity>,
        nowMillis: Long,
    ): Long = sessions.sumOf { it.durationMillis(nowMillis) }

    fun todayWorkMillis(
        sessions: List<PunchSessionEntity>,
        nowMillis: Long,
        zone: ZoneId,
    ): Long {
        val today = LocalDate.now(zone)
        return sessions
            .filter { it.startLocalDate(zone) == today }
            .sumOf { it.durationMillis(nowMillis) }
    }

    private fun PunchSessionEntity.durationMillis(nowMillis: Long): Long {
        val end = endTime ?: nowMillis
        return (end - startTime).coerceAtLeast(0L)
    }

    private fun PunchSessionEntity.startLocalDate(zone: ZoneId): LocalDate =
        Instant.ofEpochMilli(startTime)
            .atZone(zone)
            .toLocalDate()
}