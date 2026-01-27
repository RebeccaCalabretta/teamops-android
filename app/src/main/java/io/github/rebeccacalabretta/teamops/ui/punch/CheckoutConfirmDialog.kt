package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CheckoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Checkout bestätigen") },
        text = { Text("Willst du wirklich ausstempeln?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Check-out")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}