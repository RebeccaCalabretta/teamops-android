package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectViewModel @Inject constructor(
    private val objectRepository: ObjectRepository
) : ViewModel() {

    val objects: StateFlow<List<ObjectEntity>> =
        objectRepository.getAllObjects()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addObject(
        name: String,
        latitudeText: String,
        longitudeText: String,
        radiusText: String
    ) {
        val trimmedName = name.trim()
        val latitude = latitudeText.trim().toDoubleOrNull()
        val longitude = longitudeText.trim().toDoubleOrNull()
        val radius = radiusText.trim().toIntOrNull()

        if (
            trimmedName.isBlank() ||
            latitude == null ||
            longitude == null ||
            radius == null ||
            radius <= 0
        ) return

        viewModelScope.launch {
            objectRepository.upsertObject(
                ObjectEntity(
                    name = trimmedName,
                    latitude = latitude,
                    longitude = longitude,
                    radiusMeters = radius
                )
            )
        }
    }
}