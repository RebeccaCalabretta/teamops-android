package io.github.rebeccacalabretta.teamops.ui.vacation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.components.DatePickerDialog
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.util.DateTimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacationRequestSheet(
    initialStart: LocalDate? = null,
    initialEnd: LocalDate? = null,
    isEditMode: Boolean = false,
    onDismiss: () -> Unit,
    onSubmit: (LocalDate, LocalDate) -> Unit,
    onDelete: (() -> Unit)? = null
) {

    val zone = ZoneId.systemDefault()
    val nowMillis = System.currentTimeMillis()

    var startDateMillis by rememberSaveable {
        mutableLongStateOf((initialStart ?: nowMillis.toLocalDate(zone)).toStartOfDayMillis(zone))
    }

    var endDateMillis by rememberSaveable {
        mutableLongStateOf((initialEnd ?: nowMillis.toLocalDate(zone)).toStartOfDayMillis(zone))
    }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val startDate = startDateMillis.toLocalDate(zone)
    val endDate = endDateMillis.toLocalDate(zone)

    val isDateValid = !endDate.isBefore(startDate)

    val titleText = stringResource(
        if (isEditMode) R.string.vacation_edit_title else R.string.vacation_request_title
    )

    val startLabel = stringResource(R.string.start)
    val endLabel = stringResource(R.string.end)
    val submitLabel = stringResource(R.string.vacation_submit)
    val errorText = stringResource(R.string.vacation_invalid_date_range)
    val deleteLabel = stringResource(R.string.vacation_delete)

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = { showStartPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$startLabel: ${DateTimeFormat.formatFullDate(startDate)}")            }

            TextButton(
                onClick = { showEndPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$endLabel: ${DateTimeFormat.formatFullDate(endDate)}")
            }

            if (!isDateValid) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(24.dp))

            GeneralButton(
                text = submitLabel,
                onClick = { onSubmit(startDate, endDate) },
                enabled = isDateValid,
                modifier = Modifier.fillMaxWidth()
            )

            if (isEditMode && onDelete != null) {
                Spacer(Modifier.height(8.dp))

                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = deleteLabel,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    if (showStartPicker) {
        DatePickerDialog(
            initialDateMillis = startDateMillis,
            onDismiss = { showStartPicker = false },
            onDateSelected = { selected ->
                startDateMillis = selected
                if (endDateMillis < selected) {
                    endDateMillis = selected
                }
            }
        )
    }

    if (showEndPicker) {
        DatePickerDialog(
            initialDateMillis = endDateMillis,
            onDismiss = { showEndPicker = false },
            onDateSelected = { endDateMillis = it }
        )
    }
}

private fun Long.toLocalDate(zone: ZoneId): LocalDate =
    Instant.ofEpochMilli(this)
        .atZone(zone)
        .toLocalDate()

private fun LocalDate.toStartOfDayMillis(zone: ZoneId): Long =
    atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()