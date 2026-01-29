package io.github.rebeccacalabretta.teamops.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.location.LocationProvider
import io.github.rebeccacalabretta.teamops.ui.model.SessionUiModel
import io.github.rebeccacalabretta.teamops.ui.model.mapToSessionUiModel
import io.github.rebeccacalabretta.teamops.util.ObjectMatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject


private const val TAG = "PunchSessionVM"

@HiltViewModel
class PunchSessionViewModel @Inject constructor(
    private val punchSessionRepository: PunchSessionRepository,
    private val objectRepository: ObjectRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _openSession = MutableStateFlow<PunchSessionEntity?>(null)
    val openSession: StateFlow<PunchSessionEntity?> = _openSession.asStateFlow()

    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth: StateFlow<YearMonth> = _selectedMonth.asStateFlow()

    fun prevMonth() = _selectedMonth.update { it.minusMonths(1) }

    fun nextMonth() = _selectedMonth.update { it.plusMonths(1) }

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    val isCheckedIn: StateFlow<Boolean> =
        openSession
            .map { it != null }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    private val monthlySessions =
        selectedMonth.flatMapLatest { month ->
            punchSessionRepository.getSessionsForMonth(month)
        }

    val sessionRows: StateFlow<List<SessionUiModel>> =
        combine(
            monthlySessions,
            objectRepository.getAllObjects()
        ) { sessions, objects ->
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

    init {
        refreshOpenSession()
    }

    private fun runWithLoading(block: suspend () -> Unit) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                block()
                refreshOpenSession()
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun checkIn() = runWithLoading {

        val location = locationProvider.getCurrentLocationOrNull()
        if (location == null) {
            Log.w(TAG, "checkIn: No location available")
            _uiMessage.value = "Standort nicht verfügbar"
            return@runWithLoading
        }

        val objects = objectRepository.getAllObjects().first()
        val matched = ObjectMatcher.matchNearestObject(objects, location)

        if (matched == null) {
            Log.w(TAG, "checkIn: No matching object found")
            _uiMessage.value = "Kein Objekt in der Nähe gefunden"
            return@runWithLoading
        }
        punchSessionRepository.checkIn(objectId = matched.id)
    }

    fun checkOut() = runWithLoading {

        val location = locationProvider.getCurrentLocationOrNull()
        if (location == null) {
            Log.w(TAG, "checkOut: No location available")
            _uiMessage.value = "Standort nicht verfügbar"
            return@runWithLoading
        }

        val objects = objectRepository.getAllObjects().first()
        val matched = ObjectMatcher.matchNearestObject(objects, location)

        if (matched == null) {
            Log.w(TAG, "checkOut: No matching object found")
            _uiMessage.value = "Kein Objekt in der Nähe gefunden"
            return@runWithLoading
        }

        punchSessionRepository.checkOut(
            endLocation = location,
            objectEntity = matched
        )
    }

    fun refreshOpenSession() {
        viewModelScope.launch {
            _openSession.value = punchSessionRepository.getOpenSessionOrNull()
        }
    }
}