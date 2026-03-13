package io.github.rebeccacalabretta.teamops.ui.model

sealed interface UiMessage {
    data object OpenSessionExists : UiMessage
    data object NoOpenSession : UiMessage
    data object LocationUnavailable : UiMessage
    data object NoMatchingObject : UiMessage
    data object UnknownError : UiMessage
}