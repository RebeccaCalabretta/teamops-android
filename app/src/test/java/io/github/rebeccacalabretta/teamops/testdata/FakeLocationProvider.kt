package io.github.rebeccacalabretta.teamops.testdata

import android.location.Location
import io.github.rebeccacalabretta.teamops.location.LocationProvider

class FakeLocationProvider : LocationProvider {
    override suspend fun getCurrentLocationOrNull(): Location? {
        return Location("test").apply {
            latitude = 0.0
            longitude = 0.0
        }
    }
}