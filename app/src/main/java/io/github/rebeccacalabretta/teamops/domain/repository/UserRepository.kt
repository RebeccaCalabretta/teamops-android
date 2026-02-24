package io.github.rebeccacalabretta.teamops.domain.repository

import io.github.rebeccacalabretta.teamops.domain.model.UserSession

interface UserRepository {
    suspend fun getUserSession(uid: String): UserSession?
}