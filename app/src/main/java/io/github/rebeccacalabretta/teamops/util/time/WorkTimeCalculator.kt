package io.github.rebeccacalabretta.teamops.util.time

import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity

object WorkTimeCalculator {

    fun monthWorkMillis(
        sessions: List<PunchSessionEntity>,
        nowMillis: Long,
    ): Long = sessions.sumOf { it.durationMillis(nowMillis) }

    private fun PunchSessionEntity.durationMillis(nowMillis: Long): Long {
        val end = endTime ?: nowMillis
        return (end - startTime).coerceAtLeast(0L)
    }
}