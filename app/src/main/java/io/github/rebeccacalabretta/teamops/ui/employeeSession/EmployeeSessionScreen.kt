package io.github.rebeccacalabretta.teamops.ui.employeeSession

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.ui.components.EmployeeSelectDropdown
import io.github.rebeccacalabretta.teamops.ui.model.SessionUiModel
import io.github.rebeccacalabretta.teamops.ui.punch.SessionHeaderRow
import io.github.rebeccacalabretta.teamops.ui.punch.SessionRow
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeSessionViewModel
import io.github.rebeccacalabretta.teamops.viewmodel.EmployeeViewModel

@Composable
fun EmployeeSessionScreen(
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel = hiltViewModel(),
    sessionViewModel: EmployeeSessionViewModel = hiltViewModel()
) {
    val employees by employeeViewModel.employees.collectAsStateWithLifecycle()
    val selectedEmployeeId by sessionViewModel.selectedEmployeeId.collectAsStateWithLifecycle()
    val sessionRows by sessionViewModel.sessionRows.collectAsStateWithLifecycle()

    var selectedSession: SessionUiModel? by remember { mutableStateOf(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmployeeSelectDropdown(
            employees = employees,
            selectedEmployeeId = selectedEmployeeId,
            onEmployeeSelected = sessionViewModel::loadSessions,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        if (sessionRows.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Noch keine Arbeitszeiten vorhanden"
                )

                Spacer(modifier = Modifier.weight(1f))

            }

        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                stickyHeader {
                    SessionHeaderRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 4.dp),
                        showEditColumn = true
                    )
                }
                items(sessionRows) { row ->
                    SessionRow(
                        row = row,
                        canEdit = true,
                        onEditClick = {
                            selectedSession = it
                            showEditDialog = true
                        }
                    )
                }
            }
        }
        val session = selectedSession
        if (showEditDialog && session != null) {
            EditSessionDialog(
                session = session,
                onDismiss = {
                    showEditDialog = false
                    selectedSession = null
                },
                onSave = { startText, endText ->
                    sessionViewModel.saveEditedSession(
                        sessionId = session.id,
                        dateMillis = session.startTime,
                        startTimeText = startText,
                        endTimeText = endText
                    )
                    showEditDialog = false
                    selectedSession = null
                }
            )
        }
    }
}