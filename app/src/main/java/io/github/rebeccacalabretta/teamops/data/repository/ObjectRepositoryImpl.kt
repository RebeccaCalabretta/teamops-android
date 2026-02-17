package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.ObjectDao
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.sample.SampleObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ObjectRepositoryImpl(
    private val dao: ObjectDao
) : ObjectRepository {
    override fun getAllObjects(): Flow<List<ObjectEntity>> =
        dao.getAllObjects()

    override suspend fun seedIfEmpty() {
        val existing = dao.getAllObjects().first()
        if (existing.isEmpty()) {
            dao.insertAll(SampleObjects.items)
        }
    }
}