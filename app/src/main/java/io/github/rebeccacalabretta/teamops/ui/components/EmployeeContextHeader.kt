package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole

@Composable
fun EmployeeContextHeader(
    employeeId: String,
    currentUserId: String,
    employeeName: String,
    employeeRole: EmployeeRole?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = employeeName,
            style = MaterialTheme.typography.bodyMedium
        )

        if (employeeId != currentUserId) {
            employeeRole?.let {
                Text(
                    text = it.displayRole,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}