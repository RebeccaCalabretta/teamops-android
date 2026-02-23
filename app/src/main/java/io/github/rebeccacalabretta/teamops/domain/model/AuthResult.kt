package io.github.rebeccacalabretta.teamops.domain.model

sealed interface AuthResult {
    data object Success : AuthResult

    sealed interface Failure : AuthResult {
        data object InvalidCredentials : Failure
        data object UserNotFound : Failure
        data object NetworkError : Failure
        data object Unknown : Failure
    }
}