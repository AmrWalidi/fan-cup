package com.amrwalidi.android.fancup.service.impl

import android.net.Uri
import android.util.Log
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.UserService
import com.amrwalidi.android.fancup.service.model.UserDoc
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
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

    override suspend fun getUsers(id: String, username: String): List<UserDoc>? {
        return suspendCancellableCoroutine { continuation ->
            usersRef.orderBy("username")
                .startAt(username)
                .endAt(username + '\uf8ff')
                .get()
                .addOnSuccessListener { documents ->
                    if (continuation.isActive) {
                        val users = documents
                            .filter { it.id != id }
                            .map { it.toObject(UserDoc::class.java) }
                        continuation.resumeWith(Result.success(users))
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Error fetching user: ${e.message}")
                        continuation.resumeWith(Result.failure(e))
                    }
                }
        }
    }

    override suspend fun getUsers(): List<UserDoc>? {
        return suspendCancellableCoroutine { continuation ->
            usersRef
                .get()
                .addOnSuccessListener { documents ->
                    if (continuation.isActive) {
                        val users = documents
                            .map { it.toObject(UserDoc::class.java) }
                        continuation.resumeWith(Result.success(users))
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Error fetching user: ${e.message}")
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


    override suspend fun updateRank() {
        usersRef
            .orderBy("level", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = FirebaseFirestore.getInstance().batch()
                var rank = 1

                for (document in snapshot.documents) {
                    val userRef = document.reference
                    batch.update(userRef, "rank", rank)
                    rank++
                }

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("Ranking", "All user ranks updated successfully.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Ranking", "Error updating ranks", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Ranking", "Failed to fetch users", e)
            }
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

    override suspend fun addFriend(id: String, friend: String): Flow<Response> = callbackFlow {
        val userRef = usersRef.document(id)
        val friendRef = usersRef.document(friend)
        try {
            val user = userRef.get().await()
            val friends = user.get("friends") as? List<*> ?: emptyList<Any>()

            if (!friends.contains(friend)) {
                userRef.update("friends", FieldValue.arrayUnion(friend))
                    .addOnSuccessListener {
                        friendRef.update("friends", FieldValue.arrayUnion(id))
                            .addOnSuccessListener {
                                trySend(Response.Success("Value added successfully"))
                                close()
                            }
                    }
                    .addOnFailureListener { e ->
                        trySend(Response.Failure(e))
                        close()
                    }
            } else {
                trySend(Response.Success("Friend already exists"))
                close()
            }
        } catch (e: Exception) {
            trySend(Response.Failure(e))
            close()
        }
        awaitClose()
    }

    override suspend fun getFriends(id: String): Flow<Response> = callbackFlow {
        val userRef = usersRef.document(id)

        try {
            val user = userRef.get().await()
            val friends = user.get("friends") as? List<*> ?: emptyList<Any>()
            trySend(Response.Success(friends))
            close()
        } catch (e: Exception) {
            trySend(Response.Failure(e))
            close()
        }
        awaitClose()
    }

}