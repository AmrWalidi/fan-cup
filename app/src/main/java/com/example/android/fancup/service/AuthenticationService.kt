package com.example.android.fancup.service

interface AuthenticationService {
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}