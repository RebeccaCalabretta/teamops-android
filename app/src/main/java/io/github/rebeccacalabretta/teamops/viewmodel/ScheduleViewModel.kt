package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val objectRepository: ObjectRepository
) : ViewModel() {

    private val _employeeId = MutableStateFlow<String?>(null)
    private val _userContext = MutableStateFlow<UserContext?>(null)

    val scheduleEntries: StateFlow<List<ScheduleRowUiModel>> =
        combine(
            _employeeId.filterNotNull(),
            _userContext.filterNotNull()
        ) { employeeId, userContext ->
            employeeId to userContext
        }
            .flatMapLatest { (employeeId, userContext) ->
                combine(
                    scheduleRepository.getScheduleForEmployee(employeeId),
                    objectRepository.getAllObjects()
                ) { schedules, objects ->

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
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

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