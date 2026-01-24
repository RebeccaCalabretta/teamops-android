package io.github.rebeccacalabretta.teamops.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.punch.PunchScreen
import io.github.rebeccacalabretta.teamops.viewmodel.PunchSessionViewModel

@Composable
fun AppStart() {
    val context = LocalContext.current
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
    val latestSessions by vm.latestSessions.collectAsStateWithLifecycle()
    val isProcessing by vm.isProcessing.collectAsStateWithLifecycle()

    PunchScreen(
        isCheckedIn = isCheckedIn,
        isProcessing = isProcessing,
        onCheckInClick = { vm.checkIn() },
        onCheckOutClick = { vm.checkOut() },
        latestSessions = latestSessions
    )
}