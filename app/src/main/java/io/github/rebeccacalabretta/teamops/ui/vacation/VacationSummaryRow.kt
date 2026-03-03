package io.github.rebeccacalabretta.teamops.ui.vacation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.R
import io.github.rebeccacalabretta.teamops.domain.vacation.VacationBalance

@Composable
fun VacationSummaryRow(
    balance: VacationBalance?,
    modifier: Modifier = Modifier
) {
    if (balance == null) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.vacation_used_this_year, balance.usedDays),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = stringResource(R.string.vacation_remaining_days, balance.remainingDays),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}