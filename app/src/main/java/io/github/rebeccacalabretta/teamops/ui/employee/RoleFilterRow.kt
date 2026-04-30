package io.github.rebeccacalabretta.teamops.ui.employee

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

@Composable
fun RoleFilterRow(
    selectedRole: EmployeeRole?,
    onRoleSelected: (EmployeeRole?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .horizontalScroll(rememberScrollState())
    ) {
        FilterChip(
            selected = selectedRole == null,
            onClick = { onRoleSelected(null) },
            label = { Text(stringResource(R.string.all)) }
        )

        EmployeeRole.entries.forEach { role ->
            FilterChip(
                selected = selectedRole == role,
                onClick = { onRoleSelected(role) },
                label = { Text(role.displayRole) }
            )
        }
    }
}