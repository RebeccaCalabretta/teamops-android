package io.github.rebeccacalabretta.teamops.repository

import io.github.rebeccacalabretta.teamops.domain.model.AuthResult
import io.github.rebeccacalabretta.teamops.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthRepository : AuthRepository {

    override suspend fun login(email: String, password: String) =
        AuthResult.Success

    override fun logout() {}

    override fun observeAuthState(): Flow<String?> =
        flowOf("test-user")
}