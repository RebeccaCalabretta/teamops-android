package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.domain.schedule.ScheduleEntry

@Composable
fun ScheduleTable(
    entries: List<ScheduleEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {

        stickyHeader {
            ScheduleHeaderRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 4.dp)
            )
        }

        items(entries, key = { it.id }) { entry ->
            ScheduleRow(entry)
        }
    }
}

@Composable
fun ScheduleHeaderRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        ScheduleCell("Datum", Modifier.weight(1.2f), TextAlign.Start, true)
        ScheduleCell("Objekt", Modifier.weight(1.6f), TextAlign.Start, true)
        ScheduleCell("Start", Modifier.weight(0.8f), TextAlign.Center, true)
        ScheduleCell("Ende", Modifier.weight(0.8f), TextAlign.Center, true)
    }
}

@Composable
fun ScheduleRow(entry: ScheduleEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.04f))
            .padding(vertical = 6.dp)
    ) {
        ScheduleCell(entry.date.toString(), Modifier.weight(1.2f))
        ScheduleCell(entry.objectId, Modifier.weight(1.6f))
        ScheduleCell(entry.startTime.toString(), Modifier.weight(0.8f), TextAlign.Center)
        ScheduleCell(entry.endTime.toString(), Modifier.weight(0.8f), TextAlign.Center)
    }
}

@Composable
fun ScheduleCell(
    text: String,
    modifier: Modifier = Modifier,
    align: TextAlign = TextAlign.Start,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 4.dp),
        textAlign = align,
        style = if (isHeader)
            MaterialTheme.typography.labelMedium
        else
            MaterialTheme.typography.bodySmall
    )
}