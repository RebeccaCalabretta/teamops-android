package io.github.rebeccacalabretta.teamops.data.sample

import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity

object SampleObjects {
    val items: List<ObjectEntity> = listOf(
        ObjectEntity(
            id = "obj_001",
            name = "Lidl Emmendingen",
            latitude = 48.11126,
            longitude = 7.8658457,
            radiusMeters = 100
        ),
        ObjectEntity(
            id = "obj_002",
            name = "Café Fietz",
            latitude = 48.11290,
            longitude = 7.86081,
            radiusMeters = 100
        ),
        ObjectEntity(
            id = "0bj_003",
            name = "DM Waldkirch",
            latitude = 48.08740,
            longitude = 7.9455357,
            radiusMeters = 100
        )
    )
}