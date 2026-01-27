package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.ui.model.SessionUiModel

@Composable
fun PunchScreen(
    modifier: Modifier = Modifier,
    isCheckedIn: Boolean = false,
    isProcessing: Boolean = false,
    sessionRows: List<SessionUiModel> = emptyList(),
    onCheckInClick: () -> Unit = {},
    onCheckOutClick: () -> Unit = {}
) {
    val statusText = if (isCheckedIn) "Status: eingestempelt" else "Status: ausgestempelt"
    val buttonText =
        if (isProcessing) "Suche Standort..."
        else if (isCheckedIn) "Check Out"
        else "Check In"

    var showCheckOutDialog by rememberSaveable { mutableStateOf(false) }

    val onButtonClick = {
        if (isCheckedIn) {
            showCheckOutDialog = true
        } else {
            onCheckInClick()
        }
    }

    val listState = rememberLazyListState()
    val activeIndex = sessionRows.indexOfFirst { it.isCheckedIn }


    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0) {
            listState.animateScrollToItem(activeIndex)
        }
    }

    if (showCheckOutDialog) {
        CheckoutConfirmDialog(
            onConfirm = {
                showCheckOutDialog = false
                onCheckOutClick()
            },
            onDismiss = { showCheckOutDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (sessionRows.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Noch keine Arbeitszeiten vorhanden"
                )

                Spacer(modifier = Modifier.weight(1f))

            }

        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                stickyHeader {
                    SessionHeaderRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 4.dp)
                    )
                }
                items(sessionRows) { row ->
                    SessionRow(row = row)
                }
            }
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onButtonClick,
            enabled = !isProcessing,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 24.dp)
        ) {
            Text(text = buttonText)
        }
    }
}

