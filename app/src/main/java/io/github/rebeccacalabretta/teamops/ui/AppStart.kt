package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.viewmodel.AuthStateViewModel

@Composable
fun AppStart() {
    val authViewModel: AuthStateViewModel = hiltViewModel()
    val userIdState by authViewModel.userId.collectAsStateWithLifecycle()

    val userId = userIdState

    when {
        userId == null -> {
            LoginScreen()
        }

        userId.isEmpty() -> {
        }

        else -> {
            key(userId) {
                MainAppContent()
            }
        }
    }
}