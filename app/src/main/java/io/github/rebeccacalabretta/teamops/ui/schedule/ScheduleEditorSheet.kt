package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.ui.components.DatePickerDialog
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.ui.components.ObjectSelectDropdown
import io.github.rebeccacalabretta.teamops.ui.components.TimePickerDialog
import io.github.rebeccacalabretta.teamops.util.DateTimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEditorSheet(
    titleRes: Int,
    objects: List<ObjectEntity>,
    selectedObjectId: String,
    initialDate: Long,
    initialStart: Long,
    initialEnd: Long,
    isEditMode: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Long, Long, Long) -> Unit,
    onDelete: () -> Unit
) {

    var objectId by rememberSaveable { mutableStateOf(selectedObjectId) }
    var date by rememberSaveable { mutableLongStateOf(initialDate) }
    var startTime by rememberSaveable { mutableLongStateOf(initialStart) }
    var endTime by rememberSaveable { mutableLongStateOf(initialEnd) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val isTimeValid = endTime >= startTime
    val isValid = objectId.isNotBlank() && isTimeValid

    val titleText = stringResource(titleRes)
    val dateLabel = stringResource(R.string.schedule_date)
    val startLabel = stringResource(R.string.schedule_start)
    val endLabel = stringResource(R.string.schedule_end)
    val saveLabel = stringResource(R.string.schedule_save)
    val deleteLabel = stringResource(R.string.schedule_delete)

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(titleText, style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            ObjectSelectDropdown(
                objects = objects,
                selectedObjectId = objectId,
                onObjectSelected = { objectId = it.id },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$dateLabel: ${DateTimeFormat.formatDate(date)}")
            }

            TextButton(
                onClick = { showStartPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$startLabel: ${DateTimeFormat.formatTime(startTime)}")
            }

            TextButton(
                onClick = { showEndPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$endLabel: ${DateTimeFormat.formatTime(endTime)}")
            }

            if (!isTimeValid) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.schedule_invalid_time_range),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(24.dp))

            GeneralButton(
                text = saveLabel,
                onClick = { onSave(objectId, date, startTime, endTime) },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            )

            if (isEditMode) {
                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(deleteLabel, color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            initialDateMillis = date,
            onDismiss = { showDatePicker = false },
            onDateSelected = { date = it }
        )
    }

    if (showStartPicker) {
        TimePickerDialog(
            initialTimeMillis = startTime,
            onDismiss = { showStartPicker = false },
            onTimeSelected = { startTime = it }
        )
    }

    if (showEndPicker) {
        TimePickerDialog(
            initialTimeMillis = endTime,
            onDismiss = { showEndPicker = false },
            onTimeSelected = { endTime = it }
        )
    }
}