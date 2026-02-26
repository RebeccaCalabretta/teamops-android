package io.github.rebeccacalabretta.teamops.domain.model

sealed interface AuthResult {
    data object Success : AuthResult

    sealed interface Failure : AuthResult {
        data object InvalidCredentials : Failure
        data object NetworkError : Failure
    }
}