package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.rebeccacalabretta.teamops.ui.model.SessionUiModel
import io.github.rebeccacalabretta.teamops.util.SessionFormat

@Composable
fun EditSessionDialog(
    session: SessionUiModel,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    val startTime = remember(session.startTime) {
        mutableStateOf(SessionFormat.formatTime(session.startTime))
    }
    val endTime = remember(session.endTime) {
        mutableStateOf(SessionFormat.formatTime(session.endTime))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Session bearbeiten") },
        text = {
            androidx.compose.foundation.layout.Column {
                OutlinedTextField(
                    value = startTime.value,
                    onValueChange = { startTime.value = it },
                    label = { Text("Startzeit") }
                )
                OutlinedTextField(
                    value = endTime.value,
                    onValueChange = { endTime.value = it },
                    label = { Text("Endzeit") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(startTime.value, endTime.value) }
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