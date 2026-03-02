package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeSessionViewModel
import java.time.YearMonth

@Composable
fun EmployeeSessionContainer(
    employeeId: String,
    currentUserId: String,
    viewModel: EmployeeSessionViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        viewModel.loadSessions(employeeId)
    }

    var selectedMonth by rememberSaveable {
        mutableStateOf(YearMonth.now())
    }

    val todayWorkText = viewModel.todayWorkText
    val monthWorkText = viewModel.monthWorkText(selectedMonth)

    EmployeeSessionScreen(
        employeeId = employeeId,
        currentUserId = currentUserId,
        selectedMonth = selectedMonth,
        onPrevMonthClick = { selectedMonth = selectedMonth.minusMonths(1) },
        onNextMonthClick = { selectedMonth = selectedMonth.plusMonths(1) },
        todayWorkText = todayWorkText,
        monthWorkText = monthWorkText,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    )
}