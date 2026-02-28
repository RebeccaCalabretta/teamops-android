package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEditorSheet(
    titleRes: Int,
    initialObjectId: String,
    initialDate: Long,
    initialStart: Long,
    initialEnd: Long,
    isEditMode: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Long, Long, Long) -> Unit,
    onDelete: () -> Unit
) {

    var objectId by rememberSaveable { mutableStateOf(initialObjectId) }
    var date by rememberSaveable { mutableStateOf(initialDate) }
    var startTime by rememberSaveable { mutableStateOf(initialStart) }
    var endTime by rememberSaveable { mutableStateOf(initialEnd) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val zone = ZoneId.systemDefault()

    fun formatDate(millis: Long): String =
        Instant.ofEpochMilli(millis)
            .atZone(zone)
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    fun formatTime(millis: Long): String =
        Instant.ofEpochMilli(millis)
            .atZone(zone)
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm"))

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = objectId,
                onValueChange = { objectId = it },
                label = { Text(stringResource(R.string.schedule_object_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${stringResource(R.string.schedule_date)}: ${formatDate(date)}")
            }

            TextButton(
                onClick = { showStartPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${stringResource(R.string.schedule_start)}: ${formatTime(startTime)}")
            }

            TextButton(
                onClick = { showEndPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${stringResource(R.string.schedule_end)}: ${formatTime(endTime)}")
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSave(objectId, date, startTime, endTime) },
                enabled = objectId.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.schedule_save))
            }

            if (isEditMode) {
                Spacer(Modifier.height(12.dp))
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.schedule_delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    if (showDatePicker) {
        ScheduleDatePickerDialog(
            initialDateMillis = date,
            onDismiss = { showDatePicker = false },
            onDateSelected = { date = it }
        )
    }

    if (showStartPicker) {
        ScheduleTimePickerDialog(
            initialTimeMillis = startTime,
            onDismiss = { showStartPicker = false },
            onTimeSelected = { startTime = it }
        )
    }

    if (showEndPicker) {
        ScheduleTimePickerDialog(
            initialTimeMillis = endTime,
            onDismiss = { showEndPicker = false },
            onTimeSelected = { endTime = it }
        )
    }
}