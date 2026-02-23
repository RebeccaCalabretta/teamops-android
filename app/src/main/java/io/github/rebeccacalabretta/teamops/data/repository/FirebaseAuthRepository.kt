package io.github.rebeccacalabretta.teamops.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import io.github.rebeccacalabretta.teamops.domain.model.AuthResult
import io.github.rebeccacalabretta.teamops.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): AuthResult {
        return try {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            AuthResult.Success
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Failure.InvalidCredentials
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.Failure.UserNotFound
        } catch (e: Exception) {
            AuthResult.Failure.NetworkError
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun observeAuthState(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }
}