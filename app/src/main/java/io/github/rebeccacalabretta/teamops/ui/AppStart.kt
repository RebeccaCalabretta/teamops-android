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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.punch.PunchScreen
import io.github.rebeccacalabretta.teamops.viewmodel.PunchSessionViewModel

@Composable
fun AppStart() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }

    LaunchedEffect(Unit) {
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

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        PunchScreen(
            isCheckedIn = isCheckedIn,
            isProcessing = isProcessing,
            onCheckInClick = { vm.checkIn() },
            onCheckOutClick = { vm.checkOut() },
            sessionRows = sessionRows,
            modifier = Modifier.padding(padding)
        )
    }
}