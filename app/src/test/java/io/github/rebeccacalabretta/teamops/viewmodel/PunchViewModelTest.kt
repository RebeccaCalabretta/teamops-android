package io.github.rebeccacalabretta.teamops.viewmodel

import io.github.rebeccacalabretta.teamops.repository.FakeAuthRepository
import io.github.rebeccacalabretta.teamops.testdata.FakeLocationProvider
import io.github.rebeccacalabretta.teamops.testdata.FakeObjectRepository
import io.github.rebeccacalabretta.teamops.testdata.FakePunchRepository
import io.github.rebeccacalabretta.teamops.testdata.FakeUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PunchViewModelTest {

    @Test
    fun viewModel_canBeCreated() {
        Dispatchers.setMain(StandardTestDispatcher())

        val viewModel = PunchViewModel(
            punchSessionRepository = FakePunchRepository(),
            objectRepository = FakeObjectRepository(),
            locationProvider = FakeLocationProvider(),
            userRepository = FakeUserRepository(),
            authRepository = FakeAuthRepository()
        )

        assertNotNull(viewModel)
    }

    @Test
    fun showObjectColumn_isTrue_forWorker() {
        val viewModel = PunchViewModel(
            punchSessionRepository = FakePunchRepository(),
            objectRepository = FakeObjectRepository(),
            locationProvider = FakeLocationProvider(),
            userRepository = FakeUserRepository(),
            authRepository = FakeAuthRepository()
        )

        val result = viewModel.showObjectColumn.value

        assertTrue(result)
    }
}