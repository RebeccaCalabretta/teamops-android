package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.rebeccacalabretta.teamops.domain.menu.MenuItem

@Composable
fun DrawerMenu(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        items.forEach { item ->
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clickable { onItemClick(item)}
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }
}