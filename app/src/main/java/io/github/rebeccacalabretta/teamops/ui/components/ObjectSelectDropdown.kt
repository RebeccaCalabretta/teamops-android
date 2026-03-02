package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity

@Composable
fun ObjectSelectDropdown(
    objects: List<ObjectEntity>,
    selectedObjectId: String,
    onObjectSelected: (ObjectEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedObject = objects.firstOrNull { it.id == selectedObjectId }

    Column(modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = objects.isNotEmpty()) { expanded = !expanded },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedObject?.name ?: stringResource(R.string.schedule_object_select),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )

                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
        }

        if (expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 320.dp)
                ) {
                    items(
                        items = objects,
                        key = { it.id }
                    ) { obj ->
                        Text(
                            text = obj.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onObjectSelected(obj)
                                    expanded = false
                                }
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}