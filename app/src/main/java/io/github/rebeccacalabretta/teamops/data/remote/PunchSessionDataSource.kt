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
class RemotePunchDataSource @Inject constructor(
    firestore: FirebaseFirestore
) {
    private val punchSessions = firestore.collection("punchSessions")
    private val TAG = "RemotePunchDataSource"

    fun observePunchSessions(employeeId: String): Flow<List<PunchSessionDocument>> = callbackFlow {
        val registration = punchSessions
            .whereEqualTo("employeeId", employeeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val docs = snapshot
                    ?.documents
                    ?.mapNotNull { it.toObject(PunchSessionDocument::class.java) }
                    .orEmpty()
                Log.d(TAG, "observePunchSessions($employeeId) -> ${docs.size} docs")

                trySend(docs)
            }

        awaitClose { registration.remove() }
    }

    suspend fun upsert(session: PunchSessionDocument) {
        punchSessions.document(session.id).set(session).await()
    }

    suspend fun delete(sessionId: String) {
        punchSessions.document(sessionId).delete().await()
    }
}