package io.github.rebeccacalabretta.teamops.ui.state

sealed interface TopBarConfig {
    data class Root(val title: String) : TopBarConfig
    data class Child(val title: String) : TopBarConfig
}