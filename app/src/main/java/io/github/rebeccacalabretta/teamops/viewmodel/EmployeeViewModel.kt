package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.EmployeeEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.data.repository.EmployeeRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchRepository
import io.github.rebeccacalabretta.teamops.domain.access.canAccessEmployee
import io.github.rebeccacalabretta.teamops.ui.model.EmployeeRowUiModel
import io.github.rebeccacalabretta.teamops.ui.model.toEmployeeRowUiModel
import io.github.rebeccacalabretta.teamops.util.time.WorkTimeCalculator
import io.github.rebeccacalabretta.teamops.util.time.WorkTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.UUID
import javax.inject.Inject

data class EmployeeVisibility(
    val currentUserId: String,
    val currentRole: EmployeeRole,
    val teamMemberIds: Set<String> = emptySet()
)

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val punchSessionRepository: PunchRepository
) : ViewModel() {

    private val currentMonth = YearMonth.now()

    private val _visibility = MutableStateFlow(
        EmployeeVisibility(
            currentUserId = "",
            currentRole = EmployeeRole.WORKER,
            teamMemberIds = emptySet()
        )
    )

    val visibility: StateFlow<EmployeeVisibility> = _visibility.asStateFlow()

    fun setVisibility(value: EmployeeVisibility) {
        _visibility.value = value
    }

    private val _roleFilter = MutableStateFlow<EmployeeRole?>(null)
    val roleFilter: StateFlow<EmployeeRole?> = _roleFilter.asStateFlow()

    fun setRoleFilter(role: EmployeeRole?) {
        _roleFilter.value = role
    }

    private val baseEmployees = employeeRepository.getEmployees()

    private val visibleEmployees =
        combine(baseEmployees, visibility) { employees, v ->
            employees.filter { employee ->
                canAccessEmployee(
                    currentUserId = v.currentUserId,
                    currentRole = v.currentRole,
                    targetEmployeeId = employee.id,
                    teamMemberIds = v.teamMemberIds
                )
            }
        }

    private val filteredEmployees =
        combine(visibleEmployees, roleFilter) { employees, role ->
            if (role == null) employees
            else employees.filter { it.role == role }
        }

    val allEmployees: StateFlow<List<EmployeeEntity>> =
        employeeRepository.getEmployees()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val employeeRows: StateFlow<List<EmployeeRowUiModel>> =
        combine(
            filteredEmployees,
            punchSessionRepository.getSessionsForMonth(currentMonth)
        ) { employees, monthSessions ->
            val nowMillis = System.currentTimeMillis()

            employees.map { employee ->
                val sessionsForEmployee = monthSessions.filter { it.employeeId == employee.id }

                val monthMillis = WorkTimeCalculator.monthWorkMillis(
                    sessions = sessionsForEmployee,
                    nowMillis = nowMillis
                )

                employee.toEmployeeRowUiModel(
                    monthlyWorkText = WorkTimeFormatter.formatMillis(monthMillis)
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addEmployee(
        name: String,
        role: EmployeeRole,
        managerId: String? = null
    ) {
        val trimmedName = name.trim()

        if (trimmedName.isBlank()) return

        viewModelScope.launch {
            employeeRepository.upsertEmployee(
                EmployeeEntity(
                    id = UUID.randomUUID().toString(),
                    name = trimmedName,
                    role = role,
                    managerId = managerId
                )
            )
        }
    }
}