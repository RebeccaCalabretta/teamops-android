package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.ui.model.mapToSessionUiModel
import io.github.rebeccacalabretta.teamops.util.SessionFormat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeSessionViewModel @Inject constructor(
    private val punchSessionRepository: PunchSessionRepository,
    private val objectRepository: ObjectRepository,
) : ViewModel() {

    private val _selectedEmployeeId = MutableStateFlow<String?>(null)
    val selectedEmployeeId = _selectedEmployeeId.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    private val sessionsForEmployee =
        selectedEmployeeId
            .filterNotNull()
            .flatMapLatest { id ->
                punchSessionRepository.getSessionsForEmployee(id)
            }

    val sessionRows: StateFlow<List<SessionRowUiModel>> =
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

    fun saveEditedSession(
        sessionId: String,
        dateMillis: Long,
        startTimeText: String,
        endTimeText: String
    ) {
        val employeeId = selectedEmployeeId.value ?: return

        val startMillis = SessionFormat.parseTimeToMillis(dateMillis, startTimeText)
        val endMillis = SessionFormat.parseTimeToMillis(dateMillis, endTimeText)

        viewModelScope.launch {
            val session = punchSessionRepository
                .getSessionsForEmployee(employeeId)
                .first()
                .first { it.id == sessionId }

            punchSessionRepository.updateSession(
                session.copy(
                    startTime = startMillis,
                    endTime = endMillis
                )
            )
        }
    }
}
