package io.github.rebeccacalabretta.teamops.domain.model

sealed interface LoginError {
    data object InvalidCredentials : LoginError
    data object UserNotFound : LoginError
    data object Network : LoginError
    data object Unknown : LoginError
}