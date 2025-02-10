package com.example.android.fancup.repository

import com.example.android.fancup.database.FanCupDatabase
import com.example.android.fancup.domain.User
import com.example.android.fancup.service.impl.AuthenticationServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationRepository(private val database: FanCupDatabase, private val authService: AuthenticationServiceImpl) {
    val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    auth.currentUser?.apply {
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                            .get()
                            .addOnSuccessListener { doc ->
                                val user = doc.toObject(User::class.java)
                                if (user != null) {
                                    trySend(user) // Emit remote user
                                    //saveUserLocally(user) // Cache in Room
                                }
                            }
                            .addOnFailureListener {
                                close(it) // Close flow on error
                            }
                    }
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    fun hasUser(): Boolean {
        return authService.hasUser()
    }
}