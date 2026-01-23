package io.github.rebeccacalabretta.teamops.location

interface LocationProvider {
    suspend fun getCurrentLocationOrNull(): android.location.Location?
}