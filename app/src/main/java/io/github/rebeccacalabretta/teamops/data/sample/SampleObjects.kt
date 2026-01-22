package io.github.rebeccacalabretta.teamops.data.sample

import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity

object SampleObjects {
    val items: List<ObjectEntity> = listOf(
        ObjectEntity(
            id = "a9f14c32-e2d7-4f6a-b0c1-1234567890ab",
            name = "Lidl Emmendingen",
            latitude = 48.11126,
            longitude = 7.8658457,
            radiusMeters = 200
        ),
        ObjectEntity(
            id = "b3d5e7f1-c4a2-4980-abcd-0987654321cd",
            name = "Café Fietz",
            latitude = 48.11290,
            longitude = 7.86081,
            radiusMeters = 150
        ),
        ObjectEntity(
            id = "c7e8d9a0-f1e2-45b6-c7d8-098123456789",
            name = "DM Waldkirch",
            latitude = 48.08740,
            longitude = 7.9455357,
            radiusMeters = 250
        )
    )
}