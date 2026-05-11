package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.data.repository.ObjectRepository
import io.github.rebeccacalabretta.teamops.location.AddressGeocoder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectViewModel @Inject constructor(
    private val objectRepository: ObjectRepository,
    private val addressGeocoder: AddressGeocoder
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
        address: String,
        radiusText: String
    ) {
        val trimmedName = name.trim()
        val trimmedAddress = address.trim()
        val radius = radiusText.trim().toIntOrNull()

        if (
            trimmedName.isBlank() ||
            trimmedAddress.isBlank() ||
            radius == null ||
            radius <= 0
        ) return

        viewModelScope.launch {
            val coordinates =
                addressGeocoder.getCoordinates(trimmedAddress) ?: return@launch

            objectRepository.upsertObject(
                ObjectEntity(
                    name = trimmedName,
                    address = trimmedAddress,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude,
                    radiusMeters = radius
                )
            )
        }
    }
}