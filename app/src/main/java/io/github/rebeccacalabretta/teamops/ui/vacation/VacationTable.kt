package io.github.rebeccacalabretta.teamops.ui.vacation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationEntry
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationStatus
import io.github.rebeccacalabretta.teamops.util.DateTimeFormat

@Composable
fun VacationTable(
    entries: List<VacationEntry>,
    currentRole: EmployeeRole,
    onApprove: (VacationEntry, VacationStatus) -> Unit,
    onRowClick: (VacationEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {

        stickyHeader {
            VacationHeaderRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }

        items(entries, key = { it.id }) { entry ->
            VacationRow(
                entry = entry,
                currentRole = currentRole,
                onApprove = onApprove,
                onRowClick = { onRowClick(entry) }
            )
        }
    }
}

@Composable
private fun VacationHeaderRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VacationCell(
            text = stringResource(R.string.vacation_start),
            modifier = Modifier.weight(0.8f),
            isHeader = true
        )

        VacationCell(
            text = stringResource(R.string.vacation_end),
            modifier = Modifier.weight(0.8f),
            isHeader = true
        )

        VacationCell(
            text = stringResource(R.string.vacation_status),
            modifier = Modifier.weight(1f),
            isHeader = true
        )
    }
}

@Composable
private fun VacationRow(
    entry: VacationEntry,
    currentRole: EmployeeRole,
    onApprove: (VacationEntry, VacationStatus) -> Unit,
    onRowClick: () -> Unit
) {
    val isPrivileged =
        currentRole == EmployeeRole.HR ||
                currentRole == EmployeeRole.ADMIN

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 36.dp)
            .clickable { onRowClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        VacationCell(
            text = DateTimeFormat.formatFullDate(entry.startDate),
            modifier = Modifier.weight(0.8f)
        )

        VacationCell(
            text = DateTimeFormat.formatFullDate(entry.endDate),
            modifier = Modifier.weight(0.8f)
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                if (entry.status == VacationStatus.REQUESTED && isPrivileged) {

                    IconButton(
                        onClick = { onApprove(entry, VacationStatus.APPROVED) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.vacation_approve),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { onApprove(entry, VacationStatus.REJECTED) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.vacation_reject),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                } else {

                    Text(
                        text = stringResource(entry.status.toDisplayRes()),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )

                    if (isPrivileged) {

                        when (entry.status) {

                            VacationStatus.APPROVED -> {
                                IconButton(
                                    onClick = { onApprove(entry, VacationStatus.REJECTED) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.vacation_reject),
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            VacationStatus.REJECTED -> {
                                IconButton(
                                    onClick = { onApprove(entry, VacationStatus.APPROVED) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = stringResource(R.string.vacation_approve),
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VacationCell(
    text: String,
    modifier: Modifier = Modifier,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier.padding(end = 8.dp),
        textAlign = TextAlign.Start,
        style = if (isHeader)
            MaterialTheme.typography.labelMedium
        else
            MaterialTheme.typography.bodySmall
    )
}

private fun VacationStatus.toDisplayRes(): Int =
    when (this) {
        VacationStatus.REQUESTED -> R.string.vacation_status_requested
        VacationStatus.APPROVED -> R.string.vacation_status_approved
        VacationStatus.REJECTED -> R.string.vacation_status_rejected
    }