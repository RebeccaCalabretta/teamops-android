package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.rebeccacalabretta.teamops.R
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTimeMillis: Long,
    onDismiss: () -> Unit,
    onTimeSelected: (Long) -> Unit
) {

    val zone = ZoneId.systemDefault()
    val initial = Instant.ofEpochMilli(initialTimeMillis)
        .atZone(zone)
        .toLocalTime()

    val state = rememberTimePickerState(
        initialHour = initial.hour,
        initialMinute = initial.minute
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selected = Instant.ofEpochMilli(initialTimeMillis)
                        .atZone(zone)
                        .withHour(state.hour)
                        .withMinute(state.minute)
                        .withSecond(0)
                        .withNano(0)
                        .toInstant()
                        .toEpochMilli()

                    onTimeSelected(selected)
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
        },
        text = {
            TimePicker(state = state)
        }
    )
}