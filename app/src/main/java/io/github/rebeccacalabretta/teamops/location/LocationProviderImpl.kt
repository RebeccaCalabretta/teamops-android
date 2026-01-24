package io.github.rebeccacalabretta.teamops.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

private const val TAG = "LocationProvider"

class LocationProviderImpl(
    private val context: Context
) : LocationProvider {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocationOrNull(): Location? {
        return try {
            client.lastLocation.await()
                ?: client.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).await()
        } catch (e: Exception) {
            Log.d(TAG, "Location error: ${e.message} ")
            null
        }
    }
}