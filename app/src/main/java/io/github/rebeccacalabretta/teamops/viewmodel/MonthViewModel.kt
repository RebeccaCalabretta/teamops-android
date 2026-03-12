package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.YearMonth

abstract class MonthViewModel : ViewModel() {

    private val _selectedMonth =
        MutableStateFlow(YearMonth.now())

    val selectedMonth: StateFlow<YearMonth> =
        _selectedMonth.asStateFlow()

    fun prevMonth() {
        _selectedMonth.update { it.minusMonths(1) }
    }

    fun nextMonth() {
        _selectedMonth.update { it.plusMonths(1) }
    }
}