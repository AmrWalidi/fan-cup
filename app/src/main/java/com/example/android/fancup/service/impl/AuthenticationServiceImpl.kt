package com.example.android.fancup.service.impl

import com.example.android.fancup.service.AuthenticationService
import com.example.android.fancup.service.Response
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override suspend fun signIn(email: String, password: String): Flow<Response> = callbackFlow {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success(it.result.user!!.uid))
            } else {
                val errorMessage = when (it.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password!"
                    is FirebaseAuthException -> "Auth error: ${(it.exception as FirebaseAuthException).message}"
                    else -> "Unknown error: ${it.exception?.message}"
                }
                trySend(Response.Failure(Exception(errorMessage)))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun register(email: String, password: String): Flow<Response> = callbackFlow {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success(it.result.user!!.uid))
            } else {
                val errorMessage = when (it.exception) {
                    is FirebaseAuthUserCollisionException -> "This email already have an account"
                    is FirebaseAuthException -> "Auth error: ${(it.exception as FirebaseAuthException).message}"
                    else -> "Unknown error: ${it.exception?.message}"
                }
                trySend(Response.Failure(Exception(errorMessage)))
            }
            close()
        }
        awaitClose()

    }


    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

    override suspend fun resetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).await()
    }

}