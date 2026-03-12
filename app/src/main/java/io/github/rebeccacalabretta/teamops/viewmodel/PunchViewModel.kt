package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository
import io.github.rebeccacalabretta.teamops.location.LocationProvider
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.ui.model.mapToSessionUiModel
import io.github.rebeccacalabretta.teamops.util.ObjectMatcher
import io.github.rebeccacalabretta.teamops.util.WorkTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PunchViewModel @Inject constructor(
    private val punchSessionRepository: PunchSessionRepository,
    private val objectRepository: ObjectRepository,
    private val locationProvider: LocationProvider,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : MonthViewModel() {

    private val currentEmployeeId: StateFlow<String?> =
        flow {
            val uid = firebaseAuth.currentUser?.uid
            if (uid == null) emit(null)
            else emit(userRepository.getUserSession(uid)?.employeeId)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val currentRole: StateFlow<EmployeeRole?> =
        flow {
            val uid = firebaseAuth.currentUser?.uid
            if (uid == null) emit(null)
            else emit(userRepository.getUserSession(uid)?.role)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val showObjectColumn: StateFlow<Boolean> =
        currentRole
            .map { role -> role == EmployeeRole.WORKER }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    private val monthlySessions =
        selectedMonth.flatMapLatest { month ->
            punchSessionRepository.getSessionsForMonth(month)
        }

    val sessionRows: StateFlow<List<SessionRowUiModel>> =
        combine(monthlySessions, objectRepository.getAllObjects()) { sessions, objects ->
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

    val isCheckedIn: StateFlow<Boolean> =
        monthlySessions
            .map { sessions -> sessions.any { it.endTime == null } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val todayWorkText: StateFlow<String> =
        monthlySessions
            .map { sessions ->
                val todayStart = LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                val todayEnd = todayStart + 86_400_000

                val todaySessions = sessions.filter {
                    it.startTime in todayStart until todayEnd
                }

                val total = todaySessions.sumOf {
                    val end = it.endTime ?: System.currentTimeMillis()
                    end - it.startTime
                }

                WorkTimeFormatter.formatMillis(total)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0 h 00 min")

    val monthWorkText: StateFlow<String> =
        monthlySessions
            .map { sessions ->
                val total = sessions.sumOf {
                    val end = it.endTime ?: System.currentTimeMillis()
                    end - it.startTime
                }
                WorkTimeFormatter.formatMillis(total)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0 h 00 min")

    fun checkIn() = runWithLoading {
        val employeeId = currentEmployeeId.value ?: return@runWithLoading

        val location = locationProvider.getCurrentLocationOrNull()
            ?: run {
                _uiMessage.value = "Location unavailable"
                return@runWithLoading
            }

        val objects = objectRepository.getAllObjects().first()
        val matched = ObjectMatcher.matchNearestObject(objects, location)
            ?: run {
                _uiMessage.value = "No matching object found"
                return@runWithLoading
            }

        punchSessionRepository.checkIn(
            objectId = matched.id,
            employeeId = employeeId,
            currentUserId = employeeId
        )
    }

    fun checkOut() = runWithLoading {
        val employeeId = currentEmployeeId.value ?: return@runWithLoading

        val location = locationProvider.getCurrentLocationOrNull()
            ?: run {
                _uiMessage.value = "Location unavailable"
                return@runWithLoading
            }

        val objects = objectRepository.getAllObjects().first()
        val matched = ObjectMatcher.matchNearestObject(objects, location)
            ?: run {
                _uiMessage.value = "No matching object found"
                return@runWithLoading
            }

        punchSessionRepository.checkOut(
            endLocation = location,
            objectEntity = matched,
            currentUserId = employeeId
        )
    }

    private fun runWithLoading(block: suspend () -> Unit) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                block()
            } catch (e: Exception) {
                _uiMessage.value = e.message
            } finally {
                _isProcessing.value = false
            }
        }
    }
}