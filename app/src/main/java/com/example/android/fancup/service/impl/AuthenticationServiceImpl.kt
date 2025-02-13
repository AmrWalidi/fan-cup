package com.example.android.fancup.service.impl

import com.example.android.fancup.service.AuthenticationService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationServiceImpl @Inject constructor() : AuthenticationService {

    override val currentUser: Flow<FirebaseUser?>
        get() = callbackFlow {
            trySendBlocking(Firebase.auth.currentUser)

            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySendBlocking(auth.currentUser)
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }


    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun register(email: String, password: String): String? {
        return suspendCancellableCoroutine { continuation ->
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    if (continuation.isActive)
                        continuation.resumeWith(Result.success(authResult.user?.uid)) // Resume coroutine with userId
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Registration failed: ${e.message}")
                        continuation.resumeWith(Result.success(null)) // Resume with null if failed
                    }
                }
        }
    }


    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }
}