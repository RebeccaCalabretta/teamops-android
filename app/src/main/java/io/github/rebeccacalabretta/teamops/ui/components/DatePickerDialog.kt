package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.rebeccacalabretta.teamops.R
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDateMillis: Long,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val zone = ZoneId.systemDefault()

    fun toUtcSafeMillis(localMillis: Long): Long {
        val localDate = Instant.ofEpochMilli(localMillis)
            .atZone(zone)
            .toLocalDate()

        return localDate
            .atTime(LocalTime.NOON)
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }

    fun utcPickerMillisToLocalStartOfDayMillis(utcMillis: Long): Long {
        val utcDate = Instant.ofEpochMilli(utcMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()

        return utcDate
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
    }

    val state = rememberDatePickerState(
        initialSelectedDateMillis = toUtcSafeMillis(initialDateMillis),
        initialDisplayedMonthMillis = toUtcSafeMillis(initialDateMillis)
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedUtc = state.selectedDateMillis ?: return@TextButton
                    onDateSelected(utcPickerMillisToLocalStartOfDayMillis(selectedUtc))
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.schedule_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.schedule_cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}