package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PunchSessionViewModel @Inject constructor(
    private val repository: PunchSessionRepository
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
        repository.getLatestSessions(limit = 40)
            .stateIn(
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

    fun checkIn(objectId: String) = runWithLoading {
        repository.checkIn(objectId)
    }

    fun checkOut() = runWithLoading {
        repository.checkOut()
    }

    fun refreshOpenSession() {
        viewModelScope.launch {
            _openSession.value = repository.getOpenSessionOrNull()
        }
    }
}