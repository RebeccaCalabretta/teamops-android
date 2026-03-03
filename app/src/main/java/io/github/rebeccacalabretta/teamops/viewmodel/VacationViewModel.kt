package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.ApproveVacationRequestUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.CalculateRemainingVacationUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.SubmitVacationRequestUseCase
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationBalance
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val repository: VacationRepository,
    private val submitVacationRequestUseCase: SubmitVacationRequestUseCase,
    private val approveVacationRequestUseCase: ApproveVacationRequestUseCase,
    private val calculateRemainingVacationUseCase: CalculateRemainingVacationUseCase
) : ViewModel() {

    private val _vacationEntries =
        MutableStateFlow<List<VacationEntry>>(emptyList())

    val vacationEntries: StateFlow<List<VacationEntry>> =
        _vacationEntries.asStateFlow()

    private val _vacationBalance =
        MutableStateFlow<VacationBalance?>(null)

    val vacationBalance: StateFlow<VacationBalance?> =
        _vacationBalance.asStateFlow()

    fun observeVacations(employeeId: String) {
        viewModelScope.launch {
            repository
                .observeVacationsForEmployee(employeeId)
                .collect { list ->
                    _vacationEntries.value = list
                    _vacationBalance.value = calculateRemainingVacationUseCase(list)
                }
        }
    }

    fun submitVacation(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        currentUserId: String,
        currentRole: EmployeeRole,
        teamMemberIds: Set<String>
    ) {
        viewModelScope.launch {
            submitVacationRequestUseCase(
                employeeId = employeeId,
                startDate = startDate,
                endDate = endDate,
                currentUserId = currentUserId,
                currentRole = currentRole,
                teamMemberIds = teamMemberIds
            )
        }
    }

    fun approveVacation(
        requestId: String,
        currentStatus: VacationStatus,
        newStatus: VacationStatus,
        currentUserId: String,
        currentRole: EmployeeRole
    ) {
        viewModelScope.launch {
            approveVacationRequestUseCase(
                requestId = requestId,
                currentStatus = currentStatus,
                newStatus = newStatus,
                currentUserId = currentUserId,
                currentRole = currentRole
            )
        }
    }
}