package io.github.rebeccacalabretta.teamops.domain.repository

import io.github.rebeccacalabretta.teamops.domain.model.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): AuthResult

    fun logout()

    fun observeAuthState(): Flow<String?>

}