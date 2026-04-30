package io.github.rebeccacalabretta.teamops.viewmodel

import android.location.Location
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchRepository
import io.github.rebeccacalabretta.teamops.domain.repository.AuthRepository
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository
import io.github.rebeccacalabretta.teamops.location.LocationProvider
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.ui.model.UiMessage
import io.github.rebeccacalabretta.teamops.ui.model.mapToSessionUiModel
import io.github.rebeccacalabretta.teamops.util.geo.ObjectMatcher
import io.github.rebeccacalabretta.teamops.util.time.WorkTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PunchViewModel @Inject constructor(
    private val punchSessionRepository: PunchRepository,
    private val objectRepository: ObjectRepository,
    private val locationProvider: LocationProvider,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : MonthViewModel() {

    private val currentEmployeeId: StateFlow<String?> =
        authRepository.observeAuthState()
            .flatMapLatest { uid ->
                flow {
                    if (uid == null) emit(null)
                    else emit(userRepository.getUserSession(uid)?.employeeId)
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val currentRole: StateFlow<EmployeeRole?> =
        authRepository.observeAuthState()
            .flatMapLatest { uid ->
                flow {
                    if (uid == null) emit(null)
                    else emit(userRepository.getUserSession(uid)?.role)
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val syncedSessions =
        currentEmployeeId
            .filterNotNull()
            .flatMapLatest { employeeId ->
                punchSessionRepository.observeAndSyncSessions(employeeId)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )

    init {
        viewModelScope.launch {
            syncedSessions.collect { }
        }
    }

    val showObjectColumn: StateFlow<Boolean> =
        currentRole
            .map { it == EmployeeRole.WORKER }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _uiMessage = MutableStateFlow<UiMessage?>(null)
    val uiMessage: StateFlow<UiMessage?> = _uiMessage.asStateFlow()

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    private val monthlySessions =
        selectedMonth.flatMapLatest { month ->
            punchSessionRepository.getSessionsForMonth(month)
        }

    val sessionRows: StateFlow<List<SessionRowUiModel>> =
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
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val isCheckedIn: StateFlow<Boolean> =
        punchSessionRepository
            .getLatestSessions(1)
            .map { sessions ->
                val session = sessions.firstOrNull()
                session != null && session.endTime == null
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val todayWorkText: StateFlow<String> =
        currentEmployeeId
            .filterNotNull()
            .flatMapLatest { employeeId ->
                punchSessionRepository.getSessionsForEmployee(employeeId)
            }
            .map { sessions ->
                val today = LocalDate.now()

                val total =
                    sessions
                        .filter {
                            val date = Instant.ofEpochMilli(it.startTime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            date == today
                        }
                        .sumOf {
                            val end = it.endTime ?: System.currentTimeMillis()
                            end - it.startTime
                        }

                WorkTimeFormatter.formatMillis(total)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0 h 00 min")

    val monthWorkText: StateFlow<String> =
        monthlySessions
            .map { sessions ->
                val total =
                    sessions.sumOf {
                        val end = it.endTime ?: System.currentTimeMillis()
                        end - it.startTime
                    }

                WorkTimeFormatter.formatMillis(total)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0 h 00 min")

    fun checkIn() = runWithLoading {

        if (isCheckedIn.value) {
            _uiMessage.value = UiMessage.OpenSessionExists
            return@runWithLoading
        }

        jumpToCurrentMonth()

        val employeeId = requireNotNull(currentEmployeeId.value)

        val location =
            locationProvider.getCurrentLocationOrNull()
                ?: run {
                    _uiMessage.value = UiMessage.LocationUnavailable
                    return@runWithLoading
                }

        val matched =
            findNearestObject(location)
                ?: run {
                    _uiMessage.value = UiMessage.NoMatchingObject
                    return@runWithLoading
                }

        punchSessionRepository.checkIn(
            objectId = matched.id,
            employeeId = employeeId,
            currentUserId = employeeId
        )
    }

    fun checkOut() = runWithLoading {

        jumpToCurrentMonth()

        val employeeId = requireNotNull(currentEmployeeId.value)

        val location =
            locationProvider.getCurrentLocationOrNull()
                ?: run {
                    _uiMessage.value = UiMessage.LocationUnavailable
                    return@runWithLoading
                }

        val matched =
            findNearestObject(location)
                ?: run {
                    _uiMessage.value = UiMessage.NoMatchingObject
                    return@runWithLoading
                }

        punchSessionRepository.checkOut(
            endLocation = location,
            objectEntity = matched,
            currentUserId = employeeId
        )
    }

    private suspend fun findNearestObject(location: Location) =
        ObjectMatcher.matchNearestObject(
            objectRepository.getAllObjects().first(),
            location
        )

    private fun runWithLoading(block: suspend () -> Unit) {

        if (_isProcessing.value) return

        viewModelScope.launch {

            _isProcessing.value = true

            try {
                block()
            } catch (e: Exception) {

                _uiMessage.value =
                    when (e.message) {
                        "OPEN_SESSION_EXISTS" -> UiMessage.OpenSessionExists
                        "NO_OPEN_SESSION" -> UiMessage.NoOpenSession
                        else -> UiMessage.UnknownError
                    }
            } finally {
                _isProcessing.value = false
            }
        }
    }
}