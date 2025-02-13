package com.example.android.fancup.service.impl

import com.example.android.fancup.service.UserService
import com.example.android.fancup.service.model.UserDoc
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {


    private val usersRef = FirebaseFirestore.getInstance().collection("Users")
    override suspend fun createUser(id: String, username: String, email: String) {
        val user = mapOf(
            "id" to id,
            "username" to username,
            "email" to email,
            "points" to 0,
            "coins" to 0,
            "level" to "0"
        )

        usersRef.document(id)
            .set(user)
            .addOnSuccessListener {
                println("User data saved successfully!")
            }
            .addOnFailureListener { e ->
                println("Error saving user data: ${e.message}")
            }
    }

    override suspend fun getUserById(id: String): UserDoc? {
        return suspendCancellableCoroutine { continuation ->
            usersRef.document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (continuation.isActive) {
                        if (document.exists()) {
                            continuation.resumeWith(Result.success(document.toObject(UserDoc::class.java)))
                        } else {
                            continuation.resumeWith(Result.success(null))
                        }
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Registration failed: ${e.message}")
                        continuation.resumeWith(Result.success(null))
                    }
                }
        }
    }

    override suspend fun getUserByUsername(username: String, callback: (UserDoc?) -> Unit) {

        usersRef.whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val user = querySnapshot.firstOrNull()?.toObject(UserDoc::class.java)
                callback(user)
            }
    }

    override suspend fun getUserByEmail(email: String, callback: (UserDoc?) -> Unit) {
        usersRef.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val user = querySnapshot.firstOrNull()?.toObject(UserDoc::class.java)
                callback(user)
            }
    }

    override suspend fun deleteAccount(id: String) {
        usersRef.document(id).delete()
    }

}