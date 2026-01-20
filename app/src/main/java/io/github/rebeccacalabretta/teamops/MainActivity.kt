package io.github.rebeccacalabretta.teamops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.github.rebeccacalabretta.teamops.ui.AppStart
import io.github.rebeccacalabretta.teamops.ui.theme.TeamOpsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamOpsTheme {
                AppStart()
            }
        }
    }
}
