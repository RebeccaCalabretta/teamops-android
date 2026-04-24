package io.github.rebeccacalabretta.teamops.ui.modifier

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.swipeMonthNavigation(
    onSwipePrevMonth: () -> Unit,
    onSwipeNextMonth: () -> Unit
) = pointerInput(Unit) {

    var totalDrag = 0f

    detectHorizontalDragGestures(
        onDragStart = {
            totalDrag = 0f
        },
        onHorizontalDrag = { change, dragAmount ->
            totalDrag += dragAmount
            change.consume()
        },
        onDragEnd = {
            when {
                totalDrag > 100 -> onSwipePrevMonth()
                totalDrag < -100 -> onSwipeNextMonth()
            }
        }
    )
}