package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.components.MonthStepper
import io.github.rebeccacalabretta.teamops.ui.components.WorkTimeSummaryRow
import java.time.YearMonth

@Composable
fun PunchHeaderSection(
    employeeId: String,
    currentUserId: String,
    employeeName: String,
    employeeRole: EmployeeRole?,
    selectedMonth: YearMonth,
    todayWorkText: String,
    monthWorkText: String,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {

    EmployeeContextHeader(
        employeeId = employeeId,
        currentUserId = currentUserId,
        employeeName = employeeName,
        employeeRole = employeeRole
    )

    MonthStepper(
        month = selectedMonth,
        onPrevMonth = onPrevMonth,
        onNextMonth = onNextMonth,
        modifier = Modifier.fillMaxWidth()
    )

    WorkTimeSummaryRow(
        todayText = stringResource(R.string.work_time_today, todayWorkText),
        monthText = stringResource(R.string.work_time_month, monthWorkText)
    )
}