package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel

@Composable
fun PunchSessionSection(
    sessionRows: List<SessionRowUiModel>,
    showObjectColumn: Boolean,
    onSwipePrevMonth: () -> Unit,
    onSwipeNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swipeModifier = modifier.pointerInput(Unit) {

        var totalDrag = 0f

        detectHorizontalDragGestures(
            onHorizontalDrag = { _, dragAmount ->
                totalDrag += dragAmount
            },
            onDragEnd = {

                when {
                    totalDrag > 120f -> onSwipePrevMonth()
                    totalDrag < -120f -> onSwipeNextMonth()
                }

                totalDrag = 0f
            }
        )
    }

    if (sessionRows.isEmpty()) {

        Column(
            modifier = swipeModifier,
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
            showObjectColumn = showObjectColumn,
            modifier = swipeModifier
        )
    }
}