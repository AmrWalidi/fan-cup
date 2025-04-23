package com.amrwalidi.android.fancup.service.impl

import android.net.Uri
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.UserService
import com.amrwalidi.android.fancup.service.model.UserDoc
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
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

    override suspend fun updatePoints(id: String, points: Int): Flow<Response> = callbackFlow {
        usersRef.document(id).update("points", points).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success("Data have been update successfully"))
            } else {
                trySend(Response.Failure(Exception("Unknown error: ${it.exception?.message}")))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun updateCoins(id: String, coins: Int): Flow<Response> = callbackFlow {
        usersRef.document(id).update("coins", coins).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success("Data have been update successfully"))
            } else {
                trySend(Response.Failure(Exception("Unknown error: ${it.exception?.message}")))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun updateLevel(id: String, level: Int): Flow<Response> = callbackFlow {
        usersRef.document(id).update("level", level).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success("Data have been update successfully"))
            } else {
                trySend(Response.Failure(Exception("Unknown error: ${it.exception?.message}")))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun deleteUser(id: String): Flow<Response> = callbackFlow {
        usersRef.document(id).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success("User deleted successfully"))
            } else {
                trySend(Response.Failure(Exception("Unknown error: ${it.exception?.message}")))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun getUserProfileImage(id: String): Flow<Response> = callbackFlow {
        val ref = FirebaseStorage.getInstance().reference.child("user-profile/$id")

        ref.metadata.addOnSuccessListener { metadata ->
            val contentType = metadata.contentType
            val ext = when (contentType) {
                "image/jpeg" -> "jpg"
                "image/png" -> "png"
                "image/webp" -> "webp"
                else -> "jpg"
            }

            val file = File.createTempFile(id, ".$ext")

            ref.getFile(file).addOnSuccessListener {
                trySend(Response.Success(file.readBytes()))
                close()
            }.addOnFailureListener {
                trySend(Response.Failure(Exception("Download failed")))
                close()
            }

        }.addOnFailureListener {
            trySend(Response.Failure(Exception("Failed to fetch metadata")))
            close()
        }

        awaitClose()
    }

    override suspend fun uploadUserProfileImage(id: String, image: Uri): Flow<Response> =
        callbackFlow {
            val storageReference = FirebaseStorage.getInstance().getReference("user-profile/$id")

            storageReference.putFile(image).addOnSuccessListener {
                trySend(Response.Success("Image Successfully uploaded"))
                close()
            }.addOnFailureListener {
                trySend(Response.Failure(Exception("Image Successfully uploaded")))
                close()
            }
            awaitClose()
        }

}