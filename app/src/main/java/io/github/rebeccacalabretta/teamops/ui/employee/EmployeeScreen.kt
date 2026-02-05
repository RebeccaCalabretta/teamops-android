package io.github.rebeccacalabretta.teamops.ui.employee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel


@Composable
fun EmployeeScreen(
    modifier: Modifier = Modifier,
    viewModel: EmployeeViewModel = hiltViewModel(),
    onEmployeeClick: (String) -> Unit
) {
    val employees = viewModel.employees.collectAsStateWithLifecycle().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (employees.isEmpty()) {
            Text(
                text = "Keine Mitarbeiter vorhanden",
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            LazyColumn {
                items(employees) { employee ->
                    EmployeeRow(
                        name = employee.name,
                        role = employee.role,
                        onClick = { onEmployeeClick(employee.id) }
                    )

                }
            }
        }
    }
}