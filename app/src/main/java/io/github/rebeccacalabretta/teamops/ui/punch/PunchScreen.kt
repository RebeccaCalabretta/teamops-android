package io.github.rebeccacalabretta.teamops.ui.punch

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.ui.components.MonthStepper
import io.github.rebeccacalabretta.teamops.ui.components.WorkTimeSummaryRow
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.ui.permission.PermissionSettingsDialog
import io.github.rebeccacalabretta.teamops.util.permission.hasLocationPermission
import io.github.rebeccacalabretta.teamops.util.permission.openAppSettings
import io.github.rebeccacalabretta.teamops.util.permission.shouldShowLocationPermissionRationale
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.PunchViewModel

@Composable
fun PunchScreen(
    employeeId: String,
    currentUserId: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    punchViewModel: PunchViewModel = hiltViewModel(),
    employeeViewModel: EmployeeViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var hasRequestedLocationPermission by rememberSaveable { mutableStateOf(false) }
    var showPermissionSettingsDialog by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            val granted = hasLocationPermission(context)
            if (!granted && hasRequestedLocationPermission) {
                val permanentlyDenied = !shouldShowLocationPermissionRationale(context)
                if (permanentlyDenied) showPermissionSettingsDialog = true
            }
        }

    LaunchedEffect(Unit) {
        hasRequestedLocationPermission = true
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val isCheckedIn by punchViewModel.isCheckedIn.collectAsStateWithLifecycle()
    val sessionRows by punchViewModel.sessionRows.collectAsStateWithLifecycle()
    val isProcessing by punchViewModel.isProcessing.collectAsStateWithLifecycle()
    val uiMessage by punchViewModel.uiMessage.collectAsStateWithLifecycle()
    val selectedMonth by punchViewModel.selectedMonth.collectAsStateWithLifecycle()
    val todayWorkText by punchViewModel.todayWorkText.collectAsStateWithLifecycle()
    val monthWorkText by punchViewModel.monthWorkText.collectAsStateWithLifecycle()
    val showObjectColumn by punchViewModel.showObjectColumn.collectAsStateWithLifecycle()

    val allEmployees by employeeViewModel.allEmployees.collectAsStateWithLifecycle()

    val employee = allEmployees.firstOrNull { it.id == employeeId }
    val employeeName = employee?.name.orEmpty()
    val employeeRole = employee?.role

    LaunchedEffect(uiMessage) {
        val message = uiMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        punchViewModel.clearUiMessage()
    }

    if (showPermissionSettingsDialog) {
        PermissionSettingsDialog(
            title = stringResource(R.string.permission_location_title),
            message = stringResource(R.string.permission_location_message),
            confirmText = stringResource(R.string.permission_open_settings),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                showPermissionSettingsDialog = false
                openAppSettings(context)
            },
            onDismiss = { showPermissionSettingsDialog = false }
        )
    }

    val statusText = if (isCheckedIn)
        stringResource(R.string.status_checked_in)
    else
        stringResource(R.string.status_checked_out)

    val buttonText =
        if (isProcessing)
            stringResource(R.string.button_search_location)
        else if (isCheckedIn)
            stringResource(R.string.button_check_out)
        else
            stringResource(R.string.button_check_in)

    var showCheckOutDialog by rememberSaveable { mutableStateOf(false) }

    val onButtonClick = {
        if (isCheckedIn) {
            if (shouldConfirmCheckout(sessionRows)) {
                showCheckOutDialog = true
            } else {
                punchViewModel.checkOut()
            }
        } else {
            punchViewModel.checkIn()
        }
    }

    val listState = rememberLazyListState()
    val activeIndex = sessionRows.indexOfFirst { it.isCheckedIn }

    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0) {
            listState.animateScrollToItem(activeIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
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
            onPrevMonth = punchViewModel::prevMonth,
            onNextMonth = punchViewModel::nextMonth,
            modifier = Modifier.fillMaxWidth()
        )

        WorkTimeSummaryRow(
            todayText = stringResource(R.string.work_time_today, todayWorkText),
            monthText = stringResource(R.string.work_time_month, monthWorkText)
        )

        if (sessionRows.isEmpty()) {

            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(stringResource(R.string.no_work_times))
                Spacer(modifier = Modifier.weight(1f))
            }

        } else {

            PunchTable(
                rows = sessionRows,
                showObjectColumn = showObjectColumn,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        GeneralButton(
            text = buttonText,
            onClick = onButtonClick,
            enabled = !isProcessing,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 24.dp)
        )
    }
}

private fun shouldConfirmCheckout(sessionRows: List<SessionRowUiModel>): Boolean {
    val active = sessionRows.firstOrNull { it.isCheckedIn } ?: return false
    val durationMillis = System.currentTimeMillis() - active.startTime
    return durationMillis >= 60_000L
}