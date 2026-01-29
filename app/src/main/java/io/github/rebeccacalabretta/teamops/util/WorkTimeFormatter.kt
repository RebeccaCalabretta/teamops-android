package io.github.rebeccacalabretta.teamops.util

import java.util.concurrent.TimeUnit

object WorkTimeFormatter {

    fun formatMillis(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes =
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(hours)

        return "%d h %02d min".format(hours, minutes)
    }
}
