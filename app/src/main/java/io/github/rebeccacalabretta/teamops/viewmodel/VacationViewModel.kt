package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.domain.repository.VacationRepository
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val repository: VacationRepository
) : ViewModel() {

    private val _vacationEntries =
        MutableStateFlow<List<VacationEntry>>(emptyList())

    val vacationEntries: StateFlow<List<VacationEntry>> =
        _vacationEntries.asStateFlow()

    fun observeVacations(employeeId: String) {
        viewModelScope.launch {
            repository
                .observeVacationsForEmployee(employeeId)
                .collect { entries ->
                    _vacationEntries.value = entries
                }
        }
    }
}