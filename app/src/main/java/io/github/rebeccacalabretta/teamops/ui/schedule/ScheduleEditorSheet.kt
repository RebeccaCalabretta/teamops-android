package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEditorSheet(
    titleRes: Int,
    initialObjectId: String,
    initialDate: Long,
    initialStart: Long,
    initialEnd: Long,
    onDismiss: () -> Unit,
    onSave: (
        objectId: String,
        date: Long,
        startTime: Long,
        endTime: Long
    ) -> Unit
) {

    var objectId by rememberSaveable { mutableStateOf(initialObjectId) }
    var date by rememberSaveable { mutableStateOf(initialDate) }
    var startTime by rememberSaveable { mutableStateOf(initialStart) }
    var endTime by rememberSaveable { mutableStateOf(initialEnd) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = objectId,
                onValueChange = { objectId = it },
                label = { Text(stringResource(R.string.schedule_object_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    onSave(objectId, date, startTime, endTime)
                },
                enabled = objectId.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.schedule_save))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}