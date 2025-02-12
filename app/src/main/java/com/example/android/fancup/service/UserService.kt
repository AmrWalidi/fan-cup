package com.example.android.fancup.service

import com.example.android.fancup.service.model.UserDoc

interface UserService {
    suspend fun createUser(id: String, username: String, email: String)
    suspend fun getUserById(id: String, callback: (UserDoc?) -> Unit)
    suspend fun getUserByUsername(username: String, callback: (UserDoc?) -> Unit)
    suspend fun getUserByEmail(email: String, callback: (UserDoc?) -> Unit)
    suspend fun deleteAccount(id: String)
}