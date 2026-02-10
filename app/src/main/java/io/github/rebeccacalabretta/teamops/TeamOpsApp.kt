package io.github.rebeccacalabretta.teamops

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.rebeccacalabretta.teamops.data.repository.EmployeeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TeamOpsApp : Application() {
    @Inject
    lateinit var employeeRepository: EmployeeRepository

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            employeeRepository.seedIfEmpty()
        }
    }
}