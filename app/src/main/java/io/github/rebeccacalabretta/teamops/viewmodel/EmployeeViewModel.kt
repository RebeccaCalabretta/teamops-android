package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.repository.EmployeeRepository
import io.github.rebeccacalabretta.teamops.data.repository.PunchSessionRepository
import io.github.rebeccacalabretta.teamops.ui.model.EmployeeRowUiModel
import io.github.rebeccacalabretta.teamops.util.WorkTimeCalculator
import io.github.rebeccacalabretta.teamops.util.WorkTimeFormatter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val punchSessionRepository: PunchSessionRepository
) : ViewModel() {

    private val currentMonth = YearMonth.now()

    val employeeRows: StateFlow<List<EmployeeRowUiModel>> =
        combine(
            employeeRepository.getEmployees(),
            punchSessionRepository.getSessionsForMonth(currentMonth)
        ) { employees, monthSessions ->
            val nowMillis = System.currentTimeMillis()

            employees.map { employee ->
                val sessionsForEmployee =
                    monthSessions.filter { it.employeeId == employee.id }

                val monthMillis =
                    WorkTimeCalculator.monthWorkMillis(
                        sessions = sessionsForEmployee,
                        nowMillis = nowMillis
                    )

                EmployeeRowUiModel(
                    id = employee.id,
                    name = employee.name,
                    monthlyWorkText = WorkTimeFormatter.formatMillis(monthMillis)
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}