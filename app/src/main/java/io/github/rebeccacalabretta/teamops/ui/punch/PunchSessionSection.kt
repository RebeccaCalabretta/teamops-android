package io.github.rebeccacalabretta.teamops.ui.punch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.ui.model.SessionRowUiModel

@Composable
fun PunchSessionSection(
    sessionRows: List<SessionRowUiModel>,
    showObjectColumn: Boolean,
    modifier: Modifier = Modifier
) {

    if (sessionRows.isEmpty()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.no_work_times)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

    } else {

        PunchTable(
            rows = sessionRows,
            showObjectColumn = showObjectColumn,
            modifier = modifier
        )
    }
}