package com.example.android.fancup.service

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {
    val currentUser: Flow<FirebaseUser?>
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Flow<Response>
    suspend fun register(email: String, password: String) : Flow<Response>
    suspend fun signOut()
    suspend fun deleteAccount()
    suspend fun resetPassword(email: String)
}