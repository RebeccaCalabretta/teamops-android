package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import io.github.rebeccacalabretta.teamops.data.repository.EmployeeRepository
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.ui.model.SessionUiModel
import io.github.rebeccacalabretta.teamops.ui.model.mapToSessionUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EmployeeSessionViewModel @Inject constructor(
    private val punchSessionRepository: PunchSessionRepository,
    private val objectRepository: ObjectRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _selectedEmployeeId = MutableStateFlow<String?>(null)
    val selectedEmployeeId = _selectedEmployeeId.asStateFlow()

    val employees: StateFlow<List<EmployeeEntity>> =
        employeeRepository.getEmployees()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val sessionsForEmployee =
        selectedEmployeeId
            .filterNotNull()
            .flatMapLatest { id ->
                punchSessionRepository.getSessionsForEmployee(id)
            }

    val sessionRows: StateFlow<List<SessionUiModel>> =
        combine(sessionsForEmployee, objectRepository.getAllObjects()) { sessions, objects ->
            val objectsById = objects.associateBy { it.id }
            sessions.map { session ->
                mapToSessionUiModel(
                    session = session,
                    obj = objectsById[session.objectId]
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun loadSessions(employeeId: String) {
        _selectedEmployeeId.value = employeeId
    }
}
