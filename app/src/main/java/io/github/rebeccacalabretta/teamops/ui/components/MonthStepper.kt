package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.rebeccacalabretta.teamops.util.time.MonthKey
import java.time.YearMonth


@Composable
fun MonthStepper(
    month: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onPrevMonth) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Vorheriger Monat"
            )
        }

        Text(
            text = MonthKey.toDisplay(month),
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Nächster Monat"
            )
        }
    }
}