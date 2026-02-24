package io.github.rebeccacalabretta.teamops.viewmodel

import io.github.rebeccacalabretta.teamops.domain.model.UserSession

data class UserSessionState(
    val isLoading: Boolean = true,
    val session: UserSession? = null
)