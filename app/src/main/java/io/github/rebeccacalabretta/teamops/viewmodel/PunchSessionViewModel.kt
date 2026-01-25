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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    val isCheckedIn: StateFlow<Boolean> =
        openSession
            .map { it != null }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    val latestSessions: StateFlow<List<PunchSessionEntity>> =
        punchSessionRepository.getLatestSessions(limit = 40)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val sessionRows: StateFlow<List<SessionUiModel>> =
        combine(
            punchSessionRepository.getLatestSessions(limit = 40),
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
        Log.d(TAG, "Check-in started")

        val location = locationProvider.getCurrentLocationOrNull()
        if (location == null) {
            Log.d(TAG, "No location available")
            return@runWithLoading
        }

        val objects = objectRepository.getAllObjects().first()
        val matched = ObjectMatcher.matchNearestObject(objects, location)

        if (matched == null) {
            Log.d(TAG, "No matching object found")
            return@runWithLoading
        }
        Log.d(TAG, "Matched object: ${matched.name}")
        punchSessionRepository.checkIn(objectId = matched.id)
    }

    fun checkOut() = runWithLoading {
        val location = locationProvider.getCurrentLocationOrNull() ?: return@runWithLoading
        val objects = objectRepository.getAllObjects().first()
        val matched = ObjectMatcher.matchNearestObject(objects, location) ?: return@runWithLoading
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