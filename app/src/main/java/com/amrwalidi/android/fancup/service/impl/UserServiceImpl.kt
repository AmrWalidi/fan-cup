package com.amrwalidi.android.fancup.service.impl

import com.amrwalidi.android.fancup.service.UserService
import com.amrwalidi.android.fancup.service.model.UserDoc
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {


    private val usersRef = FirebaseFirestore.getInstance().collection("Users")
    override suspend fun createUser(id: String, username: String, email: String) {
        val user = UserDoc()

        user.id = id
        user.username = username
        user.email = email

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
                        println("Unable to retrieve user data")
                        continuation.resumeWith(Result.failure(e))
                    }
                }
        }
    }

    override suspend fun getUserByUsername(username: String): UserDoc? {
        return suspendCancellableCoroutine { continuation ->
            usersRef.whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { documents ->
                    if (continuation.isActive) {
                        val userDoc = documents.firstOrNull()?.toObject(UserDoc::class.java)
                        continuation.resumeWith(Result.success(userDoc))
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Error fetching user: ${e.message}")
                        continuation.resumeWith(Result.success(null))
                    }
                }
        }
    }

    override suspend fun getUserByEmail(email: String): UserDoc? {
        return suspendCancellableCoroutine { continuation ->
            usersRef.whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    if (continuation.isActive) {
                        val userDoc = documents.firstOrNull()?.toObject(UserDoc::class.java)
                        continuation.resumeWith(Result.success(userDoc))
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Error fetching user: ${e.message}")
                        continuation.resumeWith(Result.success(null))
                    }
                }
        }
    }

    override suspend fun deleteAccount(id: String) {
        usersRef.document(id).delete()
    }

}