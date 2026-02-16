package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.domain.schedule.ScheduleEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() : ViewModel() {

    private val _scheduleEntries = MutableStateFlow(
        listOf(
            ScheduleEntry(
                id = "schedule_001",
                employeeId = "emp_001",
                objectId = "obj_001",
                date = LocalDate.now(),
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(12, 0)
            ),
            ScheduleEntry(
                id = "schedule_002",
                employeeId = "emp_002",
                objectId = "obj_002",
                date = LocalDate.now(),
                startTime = LocalTime.of(13, 0),
                endTime = LocalTime.of(17, 0)
            )
        )
    )

    val scheduleEntries: StateFlow<List<ScheduleEntry>> =
        _scheduleEntries.asStateFlow()
}