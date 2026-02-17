package io.github.rebeccacalabretta.teamops.ui.vacation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry

@Composable
fun VacationTable(
    entries: List<VacationEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {

        stickyHeader {
            VacationHeaderRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 4.dp)
            )
        }

        items(entries, key = { it.id }) { entry ->
            VacationRow(entry)
        }
    }
}

@Composable
fun VacationHeaderRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        VacationCell("Start", Modifier.weight(1f), TextAlign.Start, true)
        VacationCell("Ende", Modifier.weight(1f), TextAlign.Start, true)
        VacationCell("Status", Modifier.weight(1f), TextAlign.Center, true)
    }
}

@Composable
fun VacationRow(entry: VacationEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        VacationCell(entry.startDate.toString(), Modifier.weight(1f))
        VacationCell(entry.endDate.toString(), Modifier.weight(1f))
        VacationCell(entry.status.name, Modifier.weight(1f), TextAlign.Center)
    }
}

@Composable
fun VacationCell(
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