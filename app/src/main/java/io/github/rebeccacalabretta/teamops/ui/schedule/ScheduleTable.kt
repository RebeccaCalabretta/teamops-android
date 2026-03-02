package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.model.ScheduleRowUiModel
import io.github.rebeccacalabretta.teamops.util.DateTimeFormat

@Composable
fun ScheduleTable(
    entries: List<ScheduleRowUiModel>,
    onRowClick: (ScheduleRowUiModel) -> Unit,
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
            ScheduleRow(
                entry = entry,
                onClick = { onRowClick(entry) }
            )
        }
    }
}

@Composable
private fun ScheduleHeaderRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        ScheduleCell(
            text = stringResource(R.string.schedule_header_date),
            modifier = Modifier.weight(1.2f),
            align = TextAlign.Start,
            isHeader = true
        )
        ScheduleCell(
            text = stringResource(R.string.schedule_header_object),
            modifier = Modifier.weight(1.6f),
            align = TextAlign.Start,
            isHeader = true
        )
        ScheduleCell(
            text = stringResource(R.string.schedule_header_start),
            modifier = Modifier.weight(0.8f),
            align = TextAlign.Center,
            isHeader = true
        )
        ScheduleCell(
            text = stringResource(R.string.schedule_header_end),
            modifier = Modifier.weight(0.8f),
            align = TextAlign.Center,
            isHeader = true
        )
    }
}

@Composable
private fun ScheduleRow(
    entry: ScheduleRowUiModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.04f))
            .padding(4.dp)
    ) {
        ScheduleCell(
            DateTimeFormat.formatDate(entry.date),
            Modifier.weight(1.2f)
        )

        ScheduleCell(
            entry.objectName,
            Modifier.weight(1.6f)
        )

        ScheduleCell(
            DateTimeFormat.formatTime(entry.startTime),
            Modifier.weight(0.8f),
            TextAlign.Center
        )

        ScheduleCell(
            DateTimeFormat.formatTime(entry.endTime),
            Modifier.weight(0.8f),
            TextAlign.Center
        )
    }
}

@Composable
private fun ScheduleCell(
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