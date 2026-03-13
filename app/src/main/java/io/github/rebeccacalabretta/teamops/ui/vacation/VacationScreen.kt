package io.github.rebeccacalabretta.teamops.ui.vacation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.VacationViewModel

@Composable
fun VacationScreen(
    employeeId: String,
    currentUserId: String,
    currentRole: EmployeeRole,
    teamMemberIds: Set<String>,
    modifier: Modifier = Modifier,
    viewModel: VacationViewModel = hiltViewModel(),
    employeeViewModel: EmployeeViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        viewModel.observeVacations(employeeId)
    }

    val entries by viewModel.vacationEntries.collectAsStateWithLifecycle()
    val balance by viewModel.vacationBalance.collectAsStateWithLifecycle()

    val allEmployees by employeeViewModel.allEmployees.collectAsStateWithLifecycle()
    val employee = allEmployees.firstOrNull { it.id == employeeId }
    val employeeName = employee?.name.orEmpty()
    val employeeRole = employee?.role

    var showSheet by rememberSaveable { mutableStateOf(false) }
    var editingEntry by rememberSaveable { mutableStateOf<VacationEntry?>(null) }

    fun closeSheet() {
        showSheet = false
        editingEntry = null
    }

    Column(modifier = modifier.fillMaxSize()
        .padding(horizontal = 16.dp)) {

        EmployeeContextHeader(
            employeeId = employeeId,
            currentUserId = currentUserId,
            employeeName = employeeName,
            employeeRole = employeeRole
        )

        VacationSummaryRow(
            balance = balance
        )

        VacationTable(
            entries = entries,
            currentRole = currentRole,
            onApprove = { entry, newStatus ->
                viewModel.approveVacation(
                    requestId = entry.id,
                    currentStatus = entry.status,
                    newStatus = newStatus,
                    currentUserId = currentUserId,
                    currentRole = currentRole
                )
            },
            onRowClick = { entry ->
                editingEntry = entry
                showSheet = true
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        GeneralButton(
            text = stringResource(R.string.vacation_request_button),
            onClick = {
                editingEntry = null
                showSheet = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }

    if (showSheet) {

        val entry = editingEntry

        VacationRequestSheet(
            initialStart = entry?.startDate,
            initialEnd = entry?.endDate,
            isEditMode = entry != null,
            onDismiss = { closeSheet() },

            onSubmit = { start, end ->

                if (entry != null) {

                    viewModel.updateVacation(
                        requestId = entry.id,
                        startDate = start,
                        endDate = end,
                        currentUserId = currentUserId,
                        currentRole = currentRole
                    )

                } else {

                    viewModel.submitVacation(
                        employeeId = employeeId,
                        startDate = start,
                        endDate = end,
                        currentUserId = currentUserId,
                        currentRole = currentRole,
                        teamMemberIds = teamMemberIds
                    )
                }

                closeSheet()
            },

            onDelete = if (entry != null) {
                {
                    viewModel.deleteVacation(
                        requestId = entry.id,
                        currentUserId = currentUserId,
                        currentRole = currentRole
                    )

                    closeSheet()
                }
            } else null
        )
    }
}