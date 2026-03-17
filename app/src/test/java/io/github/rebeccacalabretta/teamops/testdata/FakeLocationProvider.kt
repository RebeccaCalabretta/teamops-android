package io.github.rebeccacalabretta.teamops.testdata

import android.location.Location
import io.github.rebeccacalabretta.teamops.location.LocationProvider

class FakeLocationProvider : LocationProvider {
    override suspend fun getCurrentLocationOrNull(): Location? {
        return null
    }
}