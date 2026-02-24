package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.viewmodel.AuthStateViewModel

@Composable
fun AppStart() {
    val authViewModel: AuthStateViewModel = hiltViewModel()
    val userId by authViewModel.userId.collectAsStateWithLifecycle()

    if (userId == null) {
        LoginScreen()
    } else {
        MainAppContent()
    }
}