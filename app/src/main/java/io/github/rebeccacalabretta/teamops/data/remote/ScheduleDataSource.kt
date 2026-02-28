package io.github.rebeccacalabretta.teamops.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("schedules")
    private val TAG = "ScheduleDataSource"

    fun observeSchedulesForEmployees(employeeId: String): Flow<List<ScheduleDocument>> =
        callbackFlow {
            val listener = collection
                .whereEqualTo("employeeId", employeeId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val docs = snapshot
                        ?.documents
                        ?.mapNotNull { it.toObject(ScheduleDocument::class.java)}
                        .orEmpty()
                    Log.d(TAG, "observeSchedulesForEmployees($employeeId) -> ${docs.size} docs")

                    trySend(docs)
                }
            awaitClose { listener.remove()}
        }
    suspend fun upsert(document: ScheduleDocument) {
        collection
            .document(document.id)
            .set(document)
            .await()
    }
}