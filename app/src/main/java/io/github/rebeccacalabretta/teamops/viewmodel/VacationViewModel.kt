package io.github.rebeccacalabretta.teamops.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.error.PermissionDeniedException
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.ApproveVacationRequestUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.CalculateRemainingVacationUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.DeleteVacationUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.SubmitVacationRequestUseCase
import io.github.rebeccacalabretta.teamops.domain.usecase.vacation.UpdateVacationUseCase
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationBalance
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "VacationViewModel"

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val repository: VacationRepository,
    private val submitVacationRequestUseCase: SubmitVacationRequestUseCase,
    private val approveVacationRequestUseCase: ApproveVacationRequestUseCase,
    private val calculateRemainingVacationUseCase: CalculateRemainingVacationUseCase,
    private val updateVacationUseCase: UpdateVacationUseCase,
    private val deleteVacationUseCase: DeleteVacationUseCase
) : ViewModel() {

    private val _vacationEntries =
        MutableStateFlow<List<VacationEntry>>(emptyList())

    val vacationEntries: StateFlow<List<VacationEntry>> =
        _vacationEntries.asStateFlow()

    private val _vacationBalance =
        MutableStateFlow<VacationBalance?>(null)

    val vacationBalance: StateFlow<VacationBalance?> =
        _vacationBalance.asStateFlow()

    private var observeVacationsJob: kotlinx.coroutines.Job? = null

    fun observeVacations(employeeId: String) {
        observeVacationsJob?.cancel()

        observeVacationsJob = viewModelScope.launch {
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
            try {
                Log.d(TAG, "submitVacation role=$currentRole user=$currentUserId")

                submitVacationRequestUseCase(
                    employeeId = employeeId,
                    startDate = startDate,
                    endDate = endDate,
                    currentUserId = currentUserId,
                    currentRole = currentRole,
                    teamMemberIds = teamMemberIds
                )

            } catch (e: PermissionDeniedException) {
                Log.e(TAG, "submitVacation permission denied", e)
            } catch (e: Exception) {
                Log.e(TAG, "submitVacation failed", e)
            }
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
            try {
                Log.d(TAG, "approveVacation role=$currentRole user=$currentUserId status=$currentStatus -> $newStatus")

                approveVacationRequestUseCase(
                    requestId = requestId,
                    currentStatus = currentStatus,
                    newStatus = newStatus,
                    currentUserId = currentUserId,
                    currentRole = currentRole
                )

            } catch (e: PermissionDeniedException) {
                Log.e(TAG, "approveVacation permission denied", e)
            } catch (e: Exception) {
                Log.e(TAG, "approveVacation failed", e)
            }
        }
    }

    fun updateVacation(
        requestId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        currentUserId: String,
        currentRole: EmployeeRole
    ) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "updateVacation role=$currentRole user=$currentUserId")

                updateVacationUseCase(
                    requestId = requestId,
                    startDate = startDate,
                    endDate = endDate,
                    currentUserId = currentUserId,
                    currentRole = currentRole
                )

            } catch (e: PermissionDeniedException) {
                Log.e(TAG, "updateVacation permission denied", e)
            } catch (e: Exception) {
                Log.e(TAG, "updateVacation failed", e)
            }
        }
    }

    fun deleteVacation(
        requestId: String,
        currentUserId: String,
        currentRole: EmployeeRole
    ) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "deleteVacation role=$currentRole user=$currentUserId")

                deleteVacationUseCase(
                    requestId = requestId,
                    currentUserId = currentUserId,
                    currentRole = currentRole
                )

            } catch (e: PermissionDeniedException) {
                Log.e(TAG, "deleteVacation permission denied", e)
            } catch (e: Exception) {
                Log.e(TAG, "deleteVacation failed", e)
            }
        }
    }
}