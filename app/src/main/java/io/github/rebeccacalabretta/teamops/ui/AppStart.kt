package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> {
            key(userId) {
                MainAppContent()
            }
        }
    }
}