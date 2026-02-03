package io.github.rebeccacalabretta.teamops.ui.punch

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.permission.PermissionSettingsDialog
import io.github.rebeccacalabretta.teamops.util.permission.hasLocationPermission
import io.github.rebeccacalabretta.teamops.util.permission.openAppSettings
import io.github.rebeccacalabretta.teamops.util.permission.shouldShowLocationPermissionRationale
import io.github.rebeccacalabretta.teamops.viewmodel.PunchSessionViewModel

@Composable
fun PunchContainer(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: PunchSessionViewModel = hiltViewModel()
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

    val isCheckedIn by viewModel.isCheckedIn.collectAsStateWithLifecycle()
    val sessionRows by viewModel.sessionRows.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isProcessing.collectAsStateWithLifecycle()
    val uiMessage by viewModel.uiMessage.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val todayWorkText by viewModel.todayWorkText.collectAsStateWithLifecycle()
    val monthWorkText by viewModel.monthWorkText.collectAsStateWithLifecycle()

    LaunchedEffect(uiMessage) {
        val message = uiMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        viewModel.clearUiMessage()
    }

    if (showPermissionSettingsDialog) {
        PermissionSettingsDialog(
            title = "Location permission required",
            message = "Please enable location in the app settings.",
            confirmText = "Open settings",
            dismissText = "Cancel",
            onConfirm = {
                showPermissionSettingsDialog = false
                openAppSettings(context)
            },
            onDismiss = { showPermissionSettingsDialog = false }
        )
    }

    PunchScreen(
        modifier = modifier,
        selectedMonth = selectedMonth,
        onPrevMonthCLick = viewModel::prevMonth,
        onNextMonthCLick = viewModel::nextMonth,
        todayWorkText = todayWorkText,
        monthWorkText = monthWorkText,
        isCheckedIn = isCheckedIn,
        isProcessing = isProcessing,
        onCheckInClick = viewModel::checkIn,
        onCheckOutClick = viewModel::checkOut,
        sessionRows = sessionRows
    )
}