package io.github.rebeccacalabretta.teamops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.rebeccacalabretta.teamops.ui.PunchScreen
import io.github.rebeccacalabretta.teamops.ui.theme.TeamOpsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamOpsTheme {
                PunchScreen()
            }
        }
    }
}
