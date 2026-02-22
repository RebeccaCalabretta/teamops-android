package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.repository.ScheduleRepository
import io.github.rebeccacalabretta.teamops.ui.model.ScheduleRowUiModel
import io.github.rebeccacalabretta.teamops.ui.model.toScheduleRowUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _employeeId = MutableStateFlow<String?>(null)

    fun loadSchedule(employeeId: String) {
        _employeeId.value = employeeId
    }

    val scheduleEntries: StateFlow<List<ScheduleRowUiModel>> =
        _employeeId
            .flatMapLatest { id ->
                id?.let { scheduleRepository.getScheduleForEmployee(it) }
                    ?: flowOf(emptyList())
            }
            .map { list -> list.map { it.toScheduleRowUiModel() } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}