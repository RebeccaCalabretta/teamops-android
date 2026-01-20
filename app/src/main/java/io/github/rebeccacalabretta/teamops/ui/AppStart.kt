package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.rebeccacalabretta.teamops.viewmodel.PunchSessionViewModel

@Composable
fun AppStart() {
    val vm: PunchSessionViewModel = viewModel()
    val isCheckedIn by vm.isCheckedIn.collectAsStateWithLifecycle()

    PunchScreen(
        isCheckedIn = isCheckedIn,
        onCheckInClick = { vm.checkIn(objectId = "Dummy") },
        onCheckOutClick = { vm.checkOut() }
    )
}