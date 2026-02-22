package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    employeeId: String,
    currentUserId: String,
    currentRole: EmployeeRole,
    teamMemberIds: Set<String>,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
    employeeViewModel: EmployeeViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        viewModel.loadSchedule(employeeId)

        viewModel.setUserContext(
            currentUserId = currentUserId,
            currentRole = currentRole,
            teamMemberIds = teamMemberIds
        )
    }

    val scheduleEntries by viewModel
        .scheduleEntries
        .collectAsStateWithLifecycle()

    val allEmployees by employeeViewModel
        .allEmployees
        .collectAsStateWithLifecycle()

    val employee = allEmployees.firstOrNull { it.id == employeeId }
    val employeeName = employee?.name ?: ""
    val employeeRole = employee?.role

    Column(modifier = modifier) {

        if (employeeId != currentUserId) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = employeeName,
                    style = MaterialTheme.typography.titleMedium
                )

                employeeRole?.let {
                    Text(
                        text = it.displayRole,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        ScheduleTable(
            entries = scheduleEntries,
            modifier = Modifier
        )
    }
}