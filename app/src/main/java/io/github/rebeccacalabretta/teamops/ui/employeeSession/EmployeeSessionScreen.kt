package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeSelectDropdown
import io.github.rebeccacalabretta.teamops.ui.punch.SessionRow
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeSessionViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel

@Composable
fun EmployeeSessionScreen(
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel = hiltViewModel(),
    sessionViewModel: EmployeeSessionViewModel = hiltViewModel()
) {
    val employees by employeeViewModel.employees.collectAsStateWithLifecycle()
    val selectedEmployeeId by sessionViewModel.selectedEmployeeId.collectAsStateWithLifecycle()
    val sessionRows by sessionViewModel.sessionRows.collectAsStateWithLifecycle()

    Column(modifier) {

        EmployeeSelectDropdown(
            employees = employees,
            selectedEmployeeId = selectedEmployeeId,
            onEmployeeSelected = sessionViewModel::loadSessions,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (sessionRows.isEmpty()) {
                Text(
                    text = "Keine Sessions vorhanden",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn {
                    items(sessionRows) { row ->
                        SessionRow(row = row)
                    }
                }
            }
        }
    }
}