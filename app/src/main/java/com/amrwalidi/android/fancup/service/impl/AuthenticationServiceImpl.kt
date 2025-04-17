package com.amrwalidi.android.fancup.service.impl

import android.util.Log
import com.amrwalidi.android.fancup.service.AuthenticationService
import com.amrwalidi.android.fancup.service.Response
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
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

    override suspend fun deleteAccount(password: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, password)

            try {
                user.reauthenticate(credential).await()

                user.delete().await()
                Log.d("FirebaseAuth", "User account deleted successfully")
            } catch (e: Exception) {
                Log.e("FirebaseAuth", "Error during account deletion", e)
                throw e
            }
        } else {
            Log.e("FirebaseAuth", "User is null or email is missing")
        }
    }

    override suspend fun resetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).await()
    }

}