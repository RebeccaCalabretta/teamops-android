package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeSessionViewModel

@Composable
fun EmployeeSessionContainer(
    employeeId: String,
    currentUserId: String,
    viewModel: EmployeeSessionViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        viewModel.loadSessions(employeeId)
    }

    EmployeeSessionScreen(
        employeeId = employeeId,
        currentUserId = currentUserId,
        modifier = Modifier
            .fillMaxSize()
    )
}