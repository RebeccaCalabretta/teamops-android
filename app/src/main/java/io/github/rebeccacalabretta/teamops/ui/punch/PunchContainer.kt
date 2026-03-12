package io.github.rebeccacalabretta.teamops.ui.punch

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.permission.PermissionSettingsDialog
import io.github.rebeccacalabretta.teamops.util.permission.hasLocationPermission
import io.github.rebeccacalabretta.teamops.util.permission.openAppSettings
import io.github.rebeccacalabretta.teamops.util.permission.shouldShowLocationPermissionRationale
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.PunchViewModel

@Composable
fun PunchContainer(
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
    val employeeName = employee?.name ?: ""
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
            dismissText = "Cancel",
            onConfirm = {
                showPermissionSettingsDialog = false
                openAppSettings(context)
            },
            onDismiss = { showPermissionSettingsDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        EmployeeContextHeader(
            employeeId = employeeId,
            currentUserId = currentUserId,
            employeeName = employeeName,
            employeeRole = employeeRole
        )

        PunchScreen(
            selectedMonth = selectedMonth,
            onPrevMonthClick = punchViewModel::prevMonth,
            onNextMonthClick = punchViewModel::nextMonth,
            todayWorkText = todayWorkText,
            monthWorkText = monthWorkText,
            isCheckedIn = isCheckedIn,
            isProcessing = isProcessing,
            onCheckInClick = punchViewModel::checkIn,
            onCheckOutClick = punchViewModel::checkOut,
            sessionRows = sessionRows,
            showObjectColumn = showObjectColumn,
            modifier = Modifier.weight(1f)
        )
    }
}