package io.github.rebeccacalabretta.teamops.ui.employee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(
    modifier: Modifier = Modifier,
    viewModel: EmployeeViewModel = hiltViewModel(),
    onEmployeeClick: (String) -> Unit,
    onScheduleClick: (String) -> Unit,
    onVacationClick: (String) -> Unit
) {
    val employees = viewModel.employeeRows.collectAsStateWithLifecycle().value
    val selectedRole = viewModel.roleFilter.collectAsStateWithLifecycle().value
    val visibility = viewModel.visibility.collectAsStateWithLifecycle().value
    val currentRole = visibility.currentRole

    var expandedEmployeeId by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = modifier.fillMaxSize()
    ) {

        if (currentRole == EmployeeRole.HR || currentRole == EmployeeRole.ADMIN) {
            RoleFilterRow(
                selectedRole = selectedRole,
                onRoleSelected = viewModel::setRoleFilter,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        if (employees.isEmpty()) {
            Text(
                text = stringResource(R.string.no_employees),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn {
                items(
                    items = employees,
                    key = { it.id }
                ) { employee ->
                    EmployeeRow(
                        name = employee.name,
                        monthlyWorkTime = employee.monthlyWorkText,
                        expanded = expandedEmployeeId == employee.id,
                        showSchedule = employee.role == EmployeeRole.WORKER,
                        onToggleExpand = {
                            expandedEmployeeId =
                                if (expandedEmployeeId == employee.id) null else employee.id
                        },
                        onRowClick = { onEmployeeClick(employee.id) },
                        onSessionsClick = { onEmployeeClick(employee.id) },
                        onScheduleClick = { onScheduleClick(employee.id) },
                        onVacationClick = { onVacationClick(employee.id) }
                    )
                }
            }
        }
    }
}