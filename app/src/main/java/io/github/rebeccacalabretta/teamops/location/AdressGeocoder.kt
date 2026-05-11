package io.github.rebeccacalabretta.teamops.location

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double
)

@Singleton
class AddressGeocoder @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun getCoordinates(address: String): GeoCoordinates? {
        val trimmedAddress = address.trim()

        if (trimmedAddress.isBlank() || !Geocoder.isPresent()) return null

        val geocoder = Geocoder(context, Locale.getDefault())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getCoordinatesAsync(
                geocoder = geocoder,
                address = trimmedAddress
            )
        } else {
            getCoordinatesBlocking(
                geocoder = geocoder,
                address = trimmedAddress
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun getCoordinatesAsync(
        geocoder: Geocoder,
        address: String
    ): GeoCoordinates? =
        suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocationName(
                address,
                1
            ) { results ->
                val location = results.firstOrNull()

                continuation.resume(
                    location?.let {
                        GeoCoordinates(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                )
            }
        }

    @Suppress("DEPRECATION")
    private suspend fun getCoordinatesBlocking(
        geocoder: Geocoder,
        address: String
    ): GeoCoordinates? =
        withContext(Dispatchers.IO) {
            try {
                val location =
                    geocoder
                        .getFromLocationName(address, 1)
                        ?.firstOrNull()

                location?.let {
                    GeoCoordinates(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            } catch (_: IOException) {
                null
            } catch (_: IllegalArgumentException) {
                null
            }
        }
}