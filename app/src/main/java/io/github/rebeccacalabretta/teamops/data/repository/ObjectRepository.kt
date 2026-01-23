package io.github.rebeccacalabretta.teamops.data.repository

import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import kotlinx.coroutines.flow.Flow


interface ObjectRepository {
    fun getAllObjects(): Flow<List<ObjectEntity>>
}