package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity

@Composable
fun PunchScreen(
    isCheckedIn: Boolean = false,
    latestSessions: List<PunchSessionEntity> = emptyList(),
    onCheckInClick: () -> Unit = {},
    onCheckOutClick: () -> Unit = {}
) {
    val statusText = if (isCheckedIn) "Status: eingestempelt" else "Status: ausgestempelt"
    val buttonText = if (isCheckedIn) "Check Out" else "Check In"
    val onButtonClick = if (isCheckedIn) onCheckOutClick else onCheckInClick

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            stickyHeader {
                SessionHeaderRow()
            }
            items(latestSessions) { session ->
                SessionRow(session = session)
            }
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = buttonText)
        }
    }
}

