package io.github.rebeccacalabretta.teamops.testdata

import io.github.rebeccacalabretta.teamops.domain.model.UserSession
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository

class FakeUserRepository : UserRepository {
    override suspend fun getUserSession(uid: String): UserSession? {
        return null
    }
}