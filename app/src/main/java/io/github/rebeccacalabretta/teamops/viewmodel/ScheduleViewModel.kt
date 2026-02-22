package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.repository.ScheduleRepository
import io.github.rebeccacalabretta.teamops.ui.model.ScheduleRowUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _employeeId = MutableStateFlow<String?>(null)

    val scheduleEntries: StateFlow<List<ScheduleRowUiModel>> =
        _employeeId
            .filterNotNull()
            .flatMapLatest { id ->
                scheduleRepository.getScheduleForEmployee(id)
            }
            .map { entities ->
                entities.map { entity ->
                    ScheduleRowUiModel(
                        id = entity.id,
                        employeeId = entity.employeeId,
                        objectId = entity.employeeId,
                        date = Instant.ofEpochMilli(entity.date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(),
                        startTime = Instant.ofEpochMilli(entity.startTime)
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime(),
                        endTime = Instant.ofEpochMilli(entity.endTime)
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun loadSchedule(employeeId: String) {
        _employeeId.value = employeeId
    }
}