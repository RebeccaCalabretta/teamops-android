package io.github.rebeccacalabretta.teamops.testdata

import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeObjectRepository : ObjectRepository {
    override fun getAllObjects(): Flow<List<ObjectEntity>> {
        return flowOf(emptyList())
    }

    override suspend fun seedIfEmpty() {
    }
}