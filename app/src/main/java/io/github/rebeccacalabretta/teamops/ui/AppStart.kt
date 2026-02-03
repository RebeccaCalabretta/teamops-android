package io.github.rebeccacalabretta.teamops.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.rebeccacalabretta.teamops.ui.punch.PunchContainer

@Composable
fun AppStart() {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        PunchContainer(
            modifier = Modifier.padding(padding),
            snackbarHostState = snackbarHostState
        )
    }
}