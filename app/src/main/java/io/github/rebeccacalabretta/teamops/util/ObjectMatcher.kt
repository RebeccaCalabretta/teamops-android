package io.github.rebeccacalabretta.teamops.util

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity

object ObjectMatcher {
    fun matchNearestObject(
        objects: List<ObjectEntity>,
        location: Location,
        maxDistanceMeters: Double = 200.0
    ): ObjectEntity? {
        if(objects.isEmpty())return null

        var best: ObjectEntity? = null
        var bestDistance = Double.MAX_VALUE

        for (obj in objects) {
            val distance = GeoDistance.distanceInMeters(
                lat1 = location.latitude,
                lon1 = location.longitude,
                lat2 = obj.latitude,
                lon2 = obj.longitude
            )

            if (distance > maxDistanceMeters) continue

            val radius = obj.radiusMeters.toDouble()
            if (distance > radius) continue

            if (distance < bestDistance) {
                bestDistance = distance
                best = obj
            }
        }
        return best
    }
}