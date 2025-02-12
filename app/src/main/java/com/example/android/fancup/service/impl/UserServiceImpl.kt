package com.example.android.fancup.service.impl

import com.example.android.fancup.service.UserService
import com.example.android.fancup.service.model.UserDoc
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {


    private val usersRef = FirebaseFirestore.getInstance().collection("Users")
    override suspend fun createUser(id: String, username: String, email: String) {
        val user = mapOf(
            "username" to username,
            "email" to email,
            "points" to "0",
            "coins" to "0",
            "level_id" to "0"
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

    override suspend fun getUserById(id: String, callback: (UserDoc?) -> Unit) {
        usersRef.document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserDoc::class.java)
                    callback(user)
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