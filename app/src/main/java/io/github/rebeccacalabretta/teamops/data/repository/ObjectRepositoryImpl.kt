package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.ObjectDao
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import kotlinx.coroutines.flow.Flow

class ObjectRepositoryImpl(
    private val dao: ObjectDao
) : ObjectRepository {
    override fun getAllObjects(): Flow<List<ObjectEntity>> =
        dao.getAllObjects()

}