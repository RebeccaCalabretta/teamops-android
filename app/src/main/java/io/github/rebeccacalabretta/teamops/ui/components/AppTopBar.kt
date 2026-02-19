package io.github.rebeccacalabretta.teamops.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import io.github.rebeccacalabretta.teamops.ui.state.TopBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    config: TopBarConfig,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val title = when (config) {
        is TopBarConfig.Root -> config.title
        is TopBarConfig.Child -> config.title
    }

    val isChild = config is TopBarConfig.Child

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (isChild) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}