package io.github.rebeccacalabretta.teamops

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.rebeccacalabretta.teamops.data.db.ObjectDao
import io.github.rebeccacalabretta.teamops.data.sample.SampleObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TeamOpsApp : Application() {
    @Inject lateinit var objectDao: ObjectDao

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            objectDao.insertAll(SampleObjects.items)
        }
    }
}