package io.github.rebeccacalabretta.teamops.ui.objects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.ui.components.GeneralButton
import io.github.rebeccacalabretta.teamops.viewmodel.ObjectViewModel

@Composable
fun ObjectScreen(
    currentRole: EmployeeRole,
    modifier: Modifier = Modifier,
    viewModel: ObjectViewModel = hiltViewModel()
) {
    val objects by viewModel.objects.collectAsStateWithLifecycle()

    var showAddObjectSheet by rememberSaveable { mutableStateOf(false) }

    val canAddObject =
        currentRole == EmployeeRole.HR || currentRole == EmployeeRole.ADMIN

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (objects.isEmpty()) {
            Text(
                text = stringResource(R.string.no_objects),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = objects,
                    key = { it.id }
                ) { objectEntity ->
                    ObjectRow(objectEntity = objectEntity)
                }
            }
        }

        if (canAddObject) {
            GeneralButton(
                text = stringResource(R.string.object_add_button),
                onClick = { showAddObjectSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }

    if (showAddObjectSheet && canAddObject) {
        AddObjectSheet(
            onDismiss = { showAddObjectSheet = false },
            onSave = { name, latitude, longitude, radius ->
                viewModel.addObject(
                    name = name,
                    latitudeText = latitude,
                    longitudeText = longitude,
                    radiusText = radius
                )
                showAddObjectSheet = false
            }
        )
    }
}

@Composable
private fun ObjectRow(
    objectEntity: ObjectEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
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
                text = "Lat: ${objectEntity.latitude}, Lon: ${objectEntity.longitude}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Radius: ${objectEntity.radiusMeters} m",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}