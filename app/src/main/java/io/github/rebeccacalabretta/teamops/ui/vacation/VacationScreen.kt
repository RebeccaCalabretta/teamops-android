package io.github.rebeccacalabretta.teamops.ui.vacation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.rebeccacalabretta.teamops.viewmodel.VacationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacationScreen(
    modifier: Modifier = Modifier,
    viewModel: VacationViewModel = hiltViewModel()
) {
    val entries by viewModel.vacationEntries.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Urlaub") }
        )

        VacationTable(
            entries = entries,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}