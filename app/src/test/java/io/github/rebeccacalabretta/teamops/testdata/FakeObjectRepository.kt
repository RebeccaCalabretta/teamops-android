package io.github.rebeccacalabretta.teamops.testdata

import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeObjectRepository(
    private val objects: List<ObjectEntity> = emptyList()
) : ObjectRepository {

    override fun getAllObjects(): Flow<List<ObjectEntity>> {
        return flowOf(
            listOf(
                ObjectEntity(
                    id = "obj1",
                    name = "Test Object",
                    latitude = 0.0,
                    longitude = 0.0,
                    radiusMeters = 50
                )
            )
        )
    }

    override suspend fun seedIfEmpty() {}
}