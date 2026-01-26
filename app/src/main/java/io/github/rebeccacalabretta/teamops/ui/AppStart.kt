package io.github.rebeccacalabretta.teamops.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.permission.PermissionSettingsDialog
import io.github.rebeccacalabretta.teamops.ui.punch.PunchScreen
import io.github.rebeccacalabretta.teamops.util.permission.hasLocationPermission
import io.github.rebeccacalabretta.teamops.util.permission.openAppSettings
import io.github.rebeccacalabretta.teamops.util.permission.shouldShowLocationPermissionRationale
import io.github.rebeccacalabretta.teamops.viewmodel.PunchSessionViewModel

@Composable
fun AppStart() {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    var hasRequestedLocationPermission by rememberSaveable { mutableStateOf(false) }
    var showPermissionSettingsDialog by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            val granted = hasLocationPermission(context)
            if (!granted && hasRequestedLocationPermission) {
                val permanentlyDenied = !shouldShowLocationPermissionRationale(context)
                if (permanentlyDenied) {
                    showPermissionSettingsDialog = true
                }
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

    val vm: PunchSessionViewModel = hiltViewModel()

    val isCheckedIn by vm.isCheckedIn.collectAsStateWithLifecycle()
    val sessionRows by vm.sessionRows.collectAsStateWithLifecycle()
    val isProcessing by vm.isProcessing.collectAsStateWithLifecycle()
    val uiMessage by vm.uiMessage.collectAsStateWithLifecycle()

    LaunchedEffect(uiMessage) {
        val message = uiMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        vm.clearUiMessage()
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

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        PunchScreen(
            modifier = Modifier.padding(padding),
            isCheckedIn = isCheckedIn,
            isProcessing = isProcessing,
            onCheckInClick = { vm.checkIn() },
            onCheckOutClick = { vm.checkOut() },
            sessionRows = sessionRows
        )
    }
}