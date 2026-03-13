package io.github.rebeccacalabretta.teamops.ui.modifier

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.swipeMonthNavigation(
    onSwipePrevMonth: () -> Unit,
    onSwipeNextMonth: () -> Unit
) = pointerInput(Unit) {

    detectHorizontalDragGestures(
        onDragEnd = { },
        onHorizontalDrag = { change, dragAmount ->

            if (dragAmount > 40) {
                onSwipePrevMonth()
                change.consume()
            }

            if (dragAmount < -40) {
                onSwipeNextMonth()
                change.consume()
            }
        }
    )
}