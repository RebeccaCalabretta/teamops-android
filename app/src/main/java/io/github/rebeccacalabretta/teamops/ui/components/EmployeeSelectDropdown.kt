package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity

@Composable
fun EmployeeSelectDropdown(
    employees: List<EmployeeEntity>,
    selectedEmployeeId: String?,
    onEmployeeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedEmployee =
        employees.firstOrNull { it.id == selectedEmployeeId }

    Column(modifier) {
        Text(
            text = selectedEmployee?.name ?: "Mitarbeiter auswählen",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            employees.forEach { employee ->
                DropdownMenuItem(
                    text = { Text(employee.name) },
                    onClick = {
                        expanded = false
                        onEmployeeSelected(employee.id)
                    }
                )
            }
        }
    }
}