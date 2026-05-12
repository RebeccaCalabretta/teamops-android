package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.util.time.DateTimeFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun EditSessionDialog(
    session: SessionRowUiModel,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var startTimeText by remember(session.startTime) {
        mutableStateOf(DateTimeFormat.formatTime(session.startTime))
    }
    var endTimeText by remember(session.endTime) {
        mutableStateOf(DateTimeFormat.formatTime(session.endTime))
    }
    var errorText by remember { mutableStateOf<String?>(null) }

    val invalidStartTimeText =
        stringResource(R.string.session_error_invalid_start_time)
    val invalidEndTimeText =
        stringResource(R.string.session_error_invalid_end_time)
    val startBeforeEndText =
        stringResource(R.string.session_error_start_before_end)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.session_edit_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = startTimeText,
                    onValueChange = {
                        startTimeText = it
                        errorText = null
                    },
                    label = { Text(stringResource(R.string.session_start_time)) },
                    singleLine = true
                )

                OutlinedTextField(
                    value = endTimeText,
                    onValueChange = {
                        endTimeText = it
                        errorText = null
                    },
                    label = { Text(stringResource(R.string.session_end_time)) },
                    singleLine = true
                )

                errorText?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val error = validateStartEnd(
                        startText = startTimeText,
                        endText = endTimeText,
                        invalidStartTimeText = invalidStartTimeText,
                        invalidEndTimeText = invalidEndTimeText,
                        startBeforeEndText = startBeforeEndText
                    )
                    if (error != null) {
                        errorText = error
                    } else {
                        onSave(startTimeText.trim(), endTimeText.trim())
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private fun parseTimeOrNull(input: String): LocalTime? =
    try {
        LocalTime.parse(input.trim(), TIME_FORMATTER)
    } catch (_: DateTimeParseException) {
        null
    }

private fun validateStartEnd(
    startText: String,
    endText: String,
    invalidStartTimeText: String,
    invalidEndTimeText: String,
    startBeforeEndText: String
): String? {
    val start = parseTimeOrNull(startText)
        ?: return invalidStartTimeText

    val end = parseTimeOrNull(endText)
        ?: return invalidEndTimeText

    if (!start.isBefore(end)) return startBeforeEndText

    return null
}