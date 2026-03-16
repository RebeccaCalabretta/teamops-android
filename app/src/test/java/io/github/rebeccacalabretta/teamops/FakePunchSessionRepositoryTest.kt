package io.github.rebeccacalabretta.teamops

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FakePunchSessionRepositoryTest {

    @Test
    fun checkIn_creates_open_session() = runTest {
        val repository = FakePunchSessionRepository()

        repository.checkIn(
            objectId = "obj1",
            employeeId = "emp1",
            currentUserId = "user1"
        )

        val openSession = repository.getOpenSessionOrNull()

        assertNotNull(openSession)
        assertEquals("emp1", openSession?.employeeId)
    }

    @Test
    fun checkOut_closes_open_session() = runTest {
        val repository = FakePunchSessionRepository()

        repository.checkIn(
            objectId = "obj1",
            employeeId = "emp1",
            currentUserId = "user1"
        )

        val location = Location("test")

        val objectEntity = ObjectEntity(
            id = "obj1",
            name = "Test Object",
            latitude = 0.0,
            longitude = 0.0,
            radiusMeters = 50
        )

        repository.checkOut(
            endLocation = location,
            objectEntity = objectEntity,
            currentUserId = "user1"
        )

        val openSession = repository.getOpenSessionOrNull()

        assertNull(openSession)
    }
}
