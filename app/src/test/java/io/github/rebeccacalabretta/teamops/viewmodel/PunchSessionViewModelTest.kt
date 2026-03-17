package io.github.rebeccacalabretta.teamops.viewmodel

import io.github.rebeccacalabretta.teamops.testdata.FakeLocationProvider
import io.github.rebeccacalabretta.teamops.testdata.FakeObjectRepository
import io.github.rebeccacalabretta.teamops.testdata.FakePunchSessionRepository
import io.github.rebeccacalabretta.teamops.testdata.FakeUserRepository
import org.junit.Assert.assertNotNull
import org.junit.Test

class PunchSessionViewModelTest {

    @Test
    fun viewModel_canBeCreated() {
        val viewModel = PunchViewModel(
            punchSessionRepository = FakePunchSessionRepository(),
            objectRepository = FakeObjectRepository(),
            locationProvider = FakeLocationProvider(),
            userRepository = FakeUserRepository(),
            firebaseAuth = null!!
        )

        assertNotNull(viewModel)
    }
}