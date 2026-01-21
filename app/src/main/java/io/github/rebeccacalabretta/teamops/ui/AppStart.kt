package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.punch.PunchScreen
import io.github.rebeccacalabretta.teamops.viewmodel.PunchSessionViewModel

@Composable
fun AppStart() {
    val vm: PunchSessionViewModel = hiltViewModel()

    val isCheckedIn by vm.isCheckedIn.collectAsStateWithLifecycle()
    val latestSessions by vm.latestSessions.collectAsStateWithLifecycle()
    val isProcessing by vm.isProcessing.collectAsStateWithLifecycle()

    PunchScreen(
        isCheckedIn = isCheckedIn,
        isProcessing = isProcessing,
        onCheckInClick = { vm.checkIn(objectId = "Dummy") },
        onCheckOutClick = { vm.checkOut() },
        latestSessions = latestSessions
    )
}