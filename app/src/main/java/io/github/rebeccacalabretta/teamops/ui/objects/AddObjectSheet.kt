package io.github.rebeccacalabretta.teamops.ui.objects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddObjectSheet(
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        address: String,
        radius: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }

    val trimmedName = name.trim()
    val trimmedAddress = address.trim()
    val trimmedRadius = radius.trim()

    val isSaveEnabled =
        trimmedName.isNotBlank() &&
                trimmedAddress.isNotBlank() &&
                trimmedRadius.toIntOrNull()?.let { it > 0 } == true

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.object_add_title),
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.object_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.object_address)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = radius,
                onValueChange = { radius = it },
                label = { Text(stringResource(R.string.object_radius)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            GeneralButton(
                text = stringResource(R.string.save),
                onClick = {
                    onSave(
                        trimmedName,
                        trimmedAddress,
                        trimmedRadius
                    )
                },
                enabled = isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}