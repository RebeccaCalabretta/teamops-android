package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.ui.components.MonthStepper
import io.github.rebeccacalabretta.teamops.ui.components.WorkTimeSummaryRow
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import java.time.YearMonth

@Composable
fun PunchScreen(
    modifier: Modifier = Modifier,
    selectedMonth: YearMonth,
    onPrevMonthCLick: () -> Unit,
    onNextMonthCLick: () -> Unit,
    todayWorkText: String,
    monthWorkText: String,
    isCheckedIn: Boolean = false,
    isProcessing: Boolean = false,
    sessionRows: List<SessionRowUiModel> = emptyList(),
    onCheckInClick: () -> Unit = {},
    onCheckOutClick: () -> Unit = {}
) {
    val statusText = if (isCheckedIn)
        stringResource(R.string.status_checked_in)
    else
        stringResource(R.string.status_checked_out)

    val buttonText =
        if (isProcessing)
            stringResource(R.string.button_search_location)
        else if (isCheckedIn)
            stringResource(R.string.button_check_out)
        else
            stringResource(R.string.button_check_in)

    var showCheckOutDialog by rememberSaveable { mutableStateOf(false) }

    val onButtonClick = {
        if (isCheckedIn) {
            if (shouldConfirmCheckout(sessionRows)) {
                showCheckOutDialog = true
            } else {
                onCheckOutClick()
            }
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
        MonthStepper(
            month = selectedMonth,
            onPrevMonth = onPrevMonthCLick,
            onNextMonth = onNextMonthCLick,
            modifier = Modifier.fillMaxWidth()
        )

        WorkTimeSummaryRow(
            todayText = stringResource(R.string.work_time_today, todayWorkText),
            monthText = stringResource(R.string.work_time_month, monthWorkText)
        )

        if (sessionRows.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.no_work_times)
                )

                Spacer(modifier = Modifier.weight(1f))

            }

        } else {
            PunchTable(
                rows = sessionRows,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

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
                .fillMaxWidth(0.5f)
                .padding(bottom = 24.dp)
        )
    }
}

private fun shouldConfirmCheckout(sessionRows: List<SessionRowUiModel>): Boolean {
    val active = sessionRows.firstOrNull { it.isCheckedIn } ?: return false
    val durationMillis = System.currentTimeMillis() - active.startTime
    return durationMillis >= 60_000L
}