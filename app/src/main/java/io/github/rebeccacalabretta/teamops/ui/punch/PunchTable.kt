package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.data.db.PunchSessionEntity
import io.github.rebeccacalabretta.teamops.util.SessionFormat

@Composable
fun SessionHeaderRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Cell(
            text = "Datum",
            modifier = Modifier.weight(1f),
            align = TextAlign.Start,
            isHeader = true
        )
        Cell(
            text = "Objekt",
            modifier = Modifier.weight(1.8f),
            align = TextAlign.Start,
            isHeader = true
        )
        Cell(
            text = "Start",
            modifier = Modifier.weight(0.7f),
            align = TextAlign.End,
            isHeader = true
        )
        Cell(
            text = "Ende",
            modifier = Modifier.weight(0.7f),
            align = TextAlign.End,
            isHeader = true
        )
        Cell(
            text = "Dauer",
            modifier = Modifier.weight(0.9f),
            align = TextAlign.End,
            isHeader = true
        )
    }
}

@Composable
fun SessionRow(
    session: PunchSessionEntity,
    modifier: Modifier = Modifier
) {
    val date = SessionFormat.formatDate(session.startTime)
    val start = SessionFormat.formatTime(session.startTime)
    val end = SessionFormat.formatTime(session.endTime)
    val duration = SessionFormat.formatDuration(session.startTime, session.endTime)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Cell(
            text = date,
            modifier = Modifier.weight(1f),
            align = TextAlign.Start
        )
        Cell(
            text = session.objectId,
            modifier = Modifier.weight(1.8f),
            align = TextAlign.Start
        )
        Cell(
            text = start,
            modifier = Modifier.weight(0.7f),
            align = TextAlign.End
        )
        Cell(
            text = end,
            modifier = Modifier.weight(0.7f),
            align = TextAlign.End
        )
        Cell(
            text = duration,
            modifier = Modifier.weight(0.9f),
            align = TextAlign.End
        )
    }
}

@Composable
fun Cell(
    text: String,
    modifier: Modifier = Modifier,
    align: TextAlign = TextAlign.Start,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier,
        style = if (isHeader)
            MaterialTheme.typography.labelMedium
        else
            MaterialTheme.typography.bodySmall,
        textAlign = align,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}