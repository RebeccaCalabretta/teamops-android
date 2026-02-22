package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    employeeId: String,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    LaunchedEffect(employeeId) {
        viewModel.loadSchedule(employeeId)
    }

    val scheduleEntries by viewModel
        .scheduleEntries
        .collectAsStateWithLifecycle()

    ScheduleTable(
        entries = scheduleEntries,
        modifier = modifier
    )
}