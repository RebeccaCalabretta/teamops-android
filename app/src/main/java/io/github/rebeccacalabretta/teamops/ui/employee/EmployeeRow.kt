package io.github.rebeccacalabretta.teamops.ui.employee

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R

@Composable
fun EmployeeRow(
    name: String,
    monthlyWorkTime: String,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onRowClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onVacationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onRowClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = monthlyWorkTime,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 12.dp)
        )
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "Employee actions",
            modifier = Modifier
                .size(20.dp)
                .clickable { onToggleExpand() }
        )
    }

    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(180)
        ) + fadeIn(animationSpec = tween(120)),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(160)
        ) + fadeOut(animationSpec = tween(120))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.medium
                )
                .padding(horizontal = 12.dp),
        ) {

            ActionItem(
                text = stringResource(R.string.sessions),
                modifier = Modifier.weight(1f),
                onClick = onSessionsClick
            )

            ActionItem(
                text = stringResource(R.string.schedule),
                modifier = Modifier.weight(1f),
                onClick = onScheduleClick
            )

            ActionItem(
                text = stringResource(R.string.vacation),
                modifier = Modifier.weight(1f),
                onClick = onVacationClick
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}