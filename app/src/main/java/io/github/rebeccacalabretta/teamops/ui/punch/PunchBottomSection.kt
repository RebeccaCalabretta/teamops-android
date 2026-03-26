package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton

@Composable
fun PunchBottomSection(
    statusText: String,
    buttonText: String,
    isProcessing: Boolean,
    onButtonClick: () -> Unit
) {

    Text(
        text = statusText,
        style = MaterialTheme.typography.titleSmall
    )

    Spacer(modifier = Modifier.height(16.dp))

    GeneralButton(
        text = buttonText,
        onClick = onButtonClick,
        enabled = !isProcessing,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .testTag("punchButton")
    )
}