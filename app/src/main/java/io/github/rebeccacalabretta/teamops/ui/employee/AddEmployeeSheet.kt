package io.github.rebeccacalabretta.teamops.ui.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeSheet(
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        role: EmployeeRole
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(EmployeeRole.WORKER) }

    val trimmedName = name.trim()
    val isSaveEnabled = trimmedName.isNotBlank()

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
                text = stringResource(R.string.employee_add_title),
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.employee_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = stringResource(R.string.employee_role),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EmployeeRole.entries.forEach { role ->
                    FilterChip(
                        selected = selectedRole == role,
                        onClick = { selectedRole = role },
                        label = { Text(role.displayRole) }
                    )
                }
            }

            GeneralButton(
                text = stringResource(R.string.save),
                onClick = {
                    onSave(
                        trimmedName,
                        selectedRole
                    )
                },
                enabled = isSaveEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)            )
        }
    }
}