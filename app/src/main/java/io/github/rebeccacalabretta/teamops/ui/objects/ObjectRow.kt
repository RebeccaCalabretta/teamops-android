package io.github.rebeccacalabretta.teamops.ui.objects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity

@Composable
fun ObjectRow(
    objectEntity: ObjectEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = objectEntity.name,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = objectEntity.address,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Radius: ${objectEntity.radiusMeters} m",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}