package io.github.rebeccacalabretta.teamops.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VacationDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val collection = firestore.collection("vacationRequests")

    fun observeVacationsForEmployee(
        employeeId: String
    ): Flow<List<VacationDocument>> = callbackFlow {

        val listener = collection
            .whereEqualTo("employeeId", employeeId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val docs = snapshot
                    ?.documents
                    ?.mapNotNull { it.toObject(VacationDocument::class.java) }
                    .orEmpty()

                trySend(docs)
            }

        awaitClose { listener.remove() }
    }

    suspend fun upsert(document: VacationDocument) {
        collection
            .document(document.id)
            .set(document)
            .await()
    }

    suspend fun delete(id: String) {
        collection
            .document(id)
            .delete()
            .await()
    }
}