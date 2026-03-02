package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.components.MonthStepper
import io.github.rebeccacalabretta.teamops.ui.components.WorkTimeSummaryRow
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.ui.punch.SessionHeaderRow
import io.github.rebeccacalabretta.teamops.ui.punch.SessionRow
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeSessionViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeSessionScreen(
    employeeId: String,
    currentUserId: String,
    selectedMonth: YearMonth,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    todayWorkText: String,
    monthWorkText: String,
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel = hiltViewModel(),
    sessionViewModel: EmployeeSessionViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        sessionViewModel.loadSessions(employeeId)
    }

    val allEmployees by employeeViewModel.allEmployees.collectAsStateWithLifecycle()
    val sessionRows by sessionViewModel.sessionRows.collectAsStateWithLifecycle()

    val employee = allEmployees.firstOrNull { it.id == employeeId }
    val employeeName = employee?.name.orEmpty()
    val employeeRole = employee?.role

    var selectedSession by remember { mutableStateOf<SessionRowUiModel?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmployeeContextHeader(
            employeeId = employeeId,
            currentUserId = currentUserId,
            employeeName = employeeName,
            employeeRole = employeeRole
        )

        MonthStepper(
            month = selectedMonth,
            onPrevMonth = onPrevMonthClick,
            onNextMonth = onNextMonthClick,
            modifier = Modifier.fillMaxWidth()
        )

        WorkTimeSummaryRow(
            todayText = stringResource(R.string.work_time_today, todayWorkText),
            monthText = stringResource(R.string.work_time_month, monthWorkText)
        )

        if (sessionRows.isEmpty()) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(stringResource(R.string.no_work_times))
                Spacer(modifier = Modifier.weight(1f))
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {

                stickyHeader {
                    SessionHeaderRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 4.dp),
                        showEditColumn = true
                    )
                }

                items(sessionRows) { row ->
                    SessionRow(
                        row = row,
                        canEdit = true,
                        onEditClick = {
                            selectedSession = it
                            showEditDialog = true
                        }
                    )
                }
            }
        }
    }

    selectedSession?.let { session ->
        if (showEditDialog) {
            EditSessionDialog(
                session = session,
                onDismiss = {
                    showEditDialog = false
                    selectedSession = null
                },
                onSave = { startText, endText ->
                    sessionViewModel.saveEditedSession(
                        sessionId = session.id,
                        dateMillis = session.startTime,
                        startTimeText = startText,
                        endTimeText = endText
                    )
                    showEditDialog = false
                    selectedSession = null
                }
            )
        }
    }
}