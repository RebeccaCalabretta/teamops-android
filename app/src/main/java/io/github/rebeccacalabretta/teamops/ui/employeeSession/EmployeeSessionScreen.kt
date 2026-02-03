package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.punch.SessionRow
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeSessionViewModel

@Composable
fun EmployeeSessionScreen(
    employeeId: String,
    viewModel: EmployeeSessionViewModel = hiltViewModel()
) {
    LaunchedEffect(employeeId) {
        viewModel.loadSessions(employeeId)
    }

    val rows by viewModel.sessionRows.collectAsStateWithLifecycle()

    if (rows.isEmpty()) {
        Text("Keine Sessions vorhanden")
    } else {
        LazyColumn {
            items(rows) { row ->
                SessionRow(row = row)
            }
        }
    }
}