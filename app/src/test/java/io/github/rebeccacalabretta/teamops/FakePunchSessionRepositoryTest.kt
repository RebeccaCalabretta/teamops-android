package io.github.rebeccacalabretta.teamops

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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
}
