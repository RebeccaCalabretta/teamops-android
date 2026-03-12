package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel
import io.github.rebeccacalabretta.teamops.util.DateTimeFormat

@Composable
fun PunchTable(
    modifier: Modifier = Modifier,
    rows: List<SessionRowUiModel>,
    showObjectColumn: Boolean,
    canEdit: Boolean = false,
    onEditClick: (SessionRowUiModel) -> Unit = {},
) {
    val dateWeight = if (showObjectColumn) 0.7f else 0.7f
    val objectWeight = 1.6f
    val startWeight = if (showObjectColumn) 0.6f else 0.6f
    val endWeight = if (showObjectColumn) 0.6f else 0.6f
    val durationWeight = if (showObjectColumn) 0.9f else 2f

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        stickyHeader {
            SessionHeaderRow(
                showObjectColumn = showObjectColumn,
                showEditColumn = canEdit,
                dateWeight = dateWeight,
                objectWeight = objectWeight,
                startWeight = startWeight,
                endWeight = endWeight,
                durationWeight = durationWeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 4.dp)
            )
        }

        items(rows) { row ->
            SessionRow(
                row = row,
                showObjectColumn = showObjectColumn,
                dateWeight = dateWeight,
                objectWeight = objectWeight,
                startWeight = startWeight,
                endWeight = endWeight,
                durationWeight = durationWeight,
                canEdit = canEdit,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun SessionHeaderRow(
    showObjectColumn: Boolean,
    dateWeight: Float,
    objectWeight: Float,
    startWeight: Float,
    endWeight: Float,
    durationWeight: Float,
    modifier: Modifier = Modifier,
    showEditColumn: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Cell(
            text = "Datum",
            modifier = Modifier.weight(dateWeight),
            align = TextAlign.Start,
            isHeader = true
        )

        if (showObjectColumn) {
            Cell(
                text = "Objekt",
                modifier = Modifier.weight(objectWeight),
                align = TextAlign.Start,
                isHeader = true
            )
        }

        Cell(
            text = "Start",
            modifier = Modifier.weight(startWeight),
            align = TextAlign.Center,
            isHeader = true
        )

        Cell(
            text = "Ende",
            modifier = Modifier.weight(endWeight),
            align = TextAlign.Center,
            isHeader = true
        )

        Cell(
            text = "Dauer",
            modifier = Modifier.weight(durationWeight),
            align = TextAlign.End,
            isHeader = true
        )

        if (showEditColumn) {
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}

@Composable
fun SessionRow(
    row: SessionRowUiModel,
    showObjectColumn: Boolean,
    dateWeight: Float,
    objectWeight: Float,
    startWeight: Float,
    endWeight: Float,
    durationWeight: Float,
    modifier: Modifier = Modifier,
    canEdit: Boolean,
    onEditClick: (SessionRowUiModel) -> Unit
) {
    val date = DateTimeFormat.formatDate(row.startTime)
    val start = DateTimeFormat.formatTime(row.startTime)
    val end = DateTimeFormat.formatTime(row.endTime)
    val duration = DateTimeFormat.formatDuration(row.startTime, row.endTime)

    val textColor = if (row.isCheckedOutOutsideRadius) Color.Red else Color.Unspecified

    val background =
        if (row.isCheckedIn)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
        else
            Color.Transparent
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(vertical = 4.dp)
    ) {
        Cell(
            text = date,
            modifier = Modifier.weight(dateWeight),
            align = TextAlign.Start
        )

        if (showObjectColumn) {
            Cell(
                text = row.objectName,
                modifier = Modifier.weight(objectWeight),
                align = TextAlign.Start
            )
        }

        Cell(
            text = start,
            modifier = Modifier.weight(startWeight),
            align = TextAlign.Center
        )

        Cell(
            text = end,
            modifier = Modifier.weight(endWeight),
            align = TextAlign.Center,
            textColor = textColor
        )

        Cell(
            text = duration,
            modifier = Modifier.weight(durationWeight),
            align = TextAlign.End
        )

        if (canEdit) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .width(40.dp)
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { onEditClick(row) }
            )
        }
    }
}

@Composable
fun Cell(
    text: String,
    modifier: Modifier = Modifier,
    align: TextAlign = TextAlign.Start,
    isHeader: Boolean = false,
    textColor: Color = Color.Unspecified
) {
    Text(
        text = text,
        modifier = modifier,
        color = textColor,
        style = if (isHeader)
            MaterialTheme.typography.labelMedium
        else
            MaterialTheme.typography.bodySmall,
        textAlign = align,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}