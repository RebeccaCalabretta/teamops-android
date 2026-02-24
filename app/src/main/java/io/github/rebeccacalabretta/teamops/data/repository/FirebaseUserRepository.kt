package io.github.rebeccacalabretta.teamops.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.github.rebeccacalabretta.teamops.data.model.toEmployeeRole
import io.github.rebeccacalabretta.teamops.domain.model.UserSession
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserSession(uid: String): UserSession? =
        firestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toUserSession()
}

private fun DocumentSnapshot.toUserSession(): UserSession? {
    if (!exists()) return null

    val employeeId = getString("employeeId") ?: return null
    val role = getString("role")?.toEmployeeRole() ?: return null

    val teamMemberIds = get("teamMemberIds")
        ?.let { it as? List<*> }
        ?.filterIsInstance<String>()
        ?.toSet()
        ?: emptySet()

    return UserSession(
        employeeId = employeeId,
        role = role,
        teamMemberIds = teamMemberIds
    )
}