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
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.util.SessionFormat
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
        mutableStateOf(SessionFormat.formatTime(session.startTime))
    }
    var endTimeText by remember(session.endTime) {
        mutableStateOf(SessionFormat.formatTime(session.endTime))
    }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Session bearbeiten") },
        text = {
            Column {
                OutlinedTextField(
                    value = startTimeText,
                    onValueChange = {
                        startTimeText = it
                        errorText = null
                    },
                    label = { Text("Startzeit") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = endTimeText,
                    onValueChange = {
                        endTimeText = it
                        errorText = null
                    },
                    label = { Text("Endzeit") },
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
                    val error = validateStartEnd(startTimeText, endTimeText)
                    if (error != null) {
                        errorText = error
                    } else {
                        onSave(startTimeText.trim(), endTimeText.trim())
                    }
                }
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
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

private fun validateStartEnd(startText: String, endText: String): String? {
    val start = parseTimeOrNull(startText)
        ?: return "Bitte Startzeit im Format HH:mm eingeben (z. B. 08:30)."
    val end =
        parseTimeOrNull(endText) ?: return "Bitte Endzeit im Format HH:mm eingeben (z. B. 17:15)."
    if (!start.isBefore(end)) return "Startzeit muss vor Endzeit liegen."
    return null
}