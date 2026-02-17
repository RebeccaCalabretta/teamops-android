package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.sample.SampleVacations
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor() : ViewModel() {

    private val _vacationEntries =
        MutableStateFlow(SampleVacations.items)

    val vacationEntries: StateFlow<List<VacationEntry>> =
        _vacationEntries.asStateFlow()
}