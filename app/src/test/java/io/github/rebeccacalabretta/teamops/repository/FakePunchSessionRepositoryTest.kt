package io.github.rebeccacalabretta.teamops.repository

import android.location.Location
import io.github.rebeccacalabretta.teamops.data.db.ObjectEntity
import io.github.rebeccacalabretta.teamops.testdata.FakePunchSessionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakePunchSessionRepositoryTest {

    @Test
    fun checkIn_createsOpenSession() = runTest {
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
    fun checkOut_closesOpenSession() = runTest {
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

    @Test
    fun getOpenSessionOrNull_whenNoSessionExists_returnsNull() = runTest {
        val repository = FakePunchSessionRepository()

        val result = repository.getOpenSessionOrNull()

        assertNull(result)
    }

    @Test
    fun checkIn_throwsWhenSessionAlreadyOpen() = runTest {
        val repository = FakePunchSessionRepository()

        repository.checkIn(
            objectId = "obj1",
            employeeId = "emp1",
            currentUserId = "user1"
        )

        var exceptionThrown = false

        try {
            repository.checkIn(
                objectId = "obj1",
                employeeId = "emp1",
                currentUserId = "user1"
            )
        } catch (_: IllegalStateException) {
            exceptionThrown = true
        }

        assertTrue(exceptionThrown)
    }

    @Test
    fun checkOut_throwsWhenNoOpenSession() = runTest {
        val repository = FakePunchSessionRepository()

        val location = Location("test")

        val objectEntity = ObjectEntity(
            id = "obj1",
            name = "Test Object",
            latitude = 0.0,
            longitude = 0.0,
            radiusMeters = 50
        )

        var exceptionThrown = false
        try {
            repository.checkOut(
                endLocation = location,
                objectEntity = objectEntity,
                currentUserId = "user1"
            )
        } catch (_: IllegalStateException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)
    }
}
