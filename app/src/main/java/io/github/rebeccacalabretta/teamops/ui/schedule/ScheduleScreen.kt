package io.github.rebeccacalabretta.teamops.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.data.db.ScheduleEntity
import io.github.rebeccacalabretta.teamops.data.model.EmployeeRole
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeContextHeader
import io.github.rebeccacalabretta.teamops.ui.model.toScheduleEntity
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.ScheduleViewModel
import java.util.UUID

@Composable
fun ScheduleScreen(
    employeeId: String,
    currentUserId: String,
    currentRole: EmployeeRole,
    teamMemberIds: Set<String>,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
    employeeViewModel: EmployeeViewModel = hiltViewModel()
) {

    LaunchedEffect(employeeId) {
        viewModel.loadSchedule(employeeId)
        viewModel.setUserContext(
            currentUserId = currentUserId,
            currentRole = currentRole,
            teamMemberIds = teamMemberIds
        )
    }

    val scheduleEntries by viewModel.scheduleEntries.collectAsStateWithLifecycle()
    val allEmployees by employeeViewModel.allEmployees.collectAsStateWithLifecycle()

    val employee = allEmployees.firstOrNull { it.id == employeeId }
    val employeeName = employee?.name ?: ""
    val employeeRole = employee?.role

    var showSheet by rememberSaveable { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<ScheduleEntity?>(null) }

    Column(modifier = modifier.fillMaxSize()) {

        EmployeeContextHeader(
            employeeId = employeeId,
            currentUserId = currentUserId,
            employeeName = employeeName,
            employeeRole = employeeRole
        )

        ScheduleTable(
            entries = scheduleEntries,
            onRowClick = { row ->
                if (row.canEdit) {
                    editingEntry = row.toScheduleEntity(
                        employeeId = employeeId,
                        createdBy = currentUserId
                    )
                    showSheet = true
                }
            },
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                editingEntry = null
                showSheet = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.schedule_add_button))
        }
    }

    if (showSheet) {

        val isEdit = editingEntry != null

        ScheduleEditorSheet(
            titleRes = if (isEdit)
                R.string.schedule_edit_title
            else
                R.string.schedule_new_title,
            initialObjectId = editingEntry?.objectId ?: "",
            initialDate = editingEntry?.date ?: System.currentTimeMillis(),
            initialStart = editingEntry?.startTime ?: System.currentTimeMillis(),
            initialEnd = editingEntry?.endTime ?: (System.currentTimeMillis() + 3_600_000),
            onDismiss = { showSheet = false },
            onSave = { objectId, date, start, end ->

                val entity = ScheduleEntity(
                    id = editingEntry?.id ?: UUID.randomUUID().toString(),
                    employeeId = employeeId,
                    objectId = objectId,
                    date = date,
                    startTime = start,
                    endTime = end,
                    createdBy = editingEntry?.createdBy ?: currentUserId
                )

                viewModel.upsertSchedule(entity)
                showSheet = false
            }
        )
    }
}