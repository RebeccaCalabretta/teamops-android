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
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.VacationViewModel

@Composable
fun VacationScreen(
    employeeId: String,
    currentUserId: String,
    modifier: Modifier = Modifier,
    viewModel: VacationViewModel = hiltViewModel(),
    employeeViewModel: EmployeeViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        viewModel.observeVacations(employeeId)
    }

    val entries by viewModel.vacationEntries.collectAsStateWithLifecycle()

    val allEmployees by employeeViewModel
        .allEmployees
        .collectAsStateWithLifecycle()

    val employee = allEmployees.firstOrNull { it.id == employeeId }
    val employeeName = employee?.name.orEmpty()
    val employeeRole = employee?.role

    var showSheet by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {

        EmployeeContextHeader(
            employeeId = employeeId,
            currentUserId = currentUserId,
            employeeName = employeeName,
            employeeRole = employeeRole
        )

        VacationTable(
            entries = entries,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        GeneralButton(
            text = stringResource(R.string.vacation_request_button),
            onClick = { showSheet = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }

    if (showSheet) {
        VacationRequestSheet(
            onDismiss = { showSheet = false },
            onSubmit = { start, end ->
                viewModel.submitVacation(
                    employeeId = employeeId,
                    startDate = start,
                    endDate = end,
                    currentUserId = currentUserId
                )
                showSheet = false
            }
        )
    }
}