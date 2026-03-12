package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.data.repository.ScheduleRepository
import io.github.rebeccacalabretta.teamops.ui.model.ScheduleRowUiModel
import io.github.rebeccacalabretta.teamops.ui.model.toScheduleRowUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val objectRepository: ObjectRepository
) : MonthViewModel() {

    private val _employeeId = MutableStateFlow<String?>(null)
    private val _userContext = MutableStateFlow<UserContext?>(null)

    fun loadSchedule(employeeId: String) {
        _employeeId.value = employeeId
    }

    fun setUserContext(
        currentUserId: String,
        currentRole: EmployeeRole,
        teamMemberIds: Set<String>
    ) {
        _userContext.value = UserContext(
            currentUserId = currentUserId,
            currentRole = currentRole,
            teamMemberIds = teamMemberIds
        )
    }

    private val monthlySchedules =
        combine(
            _employeeId.filterNotNull(),
            selectedMonth
        ) { employeeId, month ->
            employeeId to month
        }.flatMapLatest { (employeeId, month) ->

            scheduleRepository
                .getScheduleForEmployee(employeeId)
                .map { schedules ->

                    schedules.filter { entity ->

                        val entityMonth =
                            Instant.ofEpochMilli(entity.date)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .let { YearMonth.from(it) }

                        entityMonth == month
                    }
                }
        }

    val scheduleEntries: StateFlow<List<ScheduleRowUiModel>> =
        combine(
            monthlySchedules,
            objectRepository.getAllObjects(),
            _userContext.filterNotNull()
        ) { schedules, objects, userContext ->

            val objectMap = objects.associateBy { it.id }

            schedules.map { entity ->

                val objectName =
                    objectMap[entity.objectId]?.name ?: "Unbekannt"

                val canEdit =
                    canEdit(entity, userContext)

                entity.toScheduleRowUiModel(
                    objectName = objectName,
                    canEdit = canEdit
                )
            }
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val objects: StateFlow<List<ObjectEntity>> =
        objectRepository.getAllObjects()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun upsertSchedule(entry: ScheduleEntity) {
        val user = _userContext.value ?: return
        viewModelScope.launch {
            scheduleRepository.upsertSchedule(
                entry = entry,
                currentUserId = user.currentUserId
            )
        }
    }

    fun deleteSchedule(entry: ScheduleEntity) {
        val user = _userContext.value ?: return
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(
                entry = entry,
                currentUserId = user.currentUserId
            )
        }
    }

    private fun canEdit(
        entity: ScheduleEntity,
        userContext: UserContext
    ): Boolean {
        return when (userContext.currentRole) {

            EmployeeRole.WORKER -> false

            EmployeeRole.MANAGER ->
                entity.employeeId in userContext.teamMemberIds

            EmployeeRole.HR,
            EmployeeRole.ADMIN -> true
        }
    }

    private data class UserContext(
        val currentUserId: String,
        val currentRole: EmployeeRole,
        val teamMemberIds: Set<String>
    )
}