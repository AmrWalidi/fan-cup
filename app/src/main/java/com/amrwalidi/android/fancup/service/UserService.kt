package com.amrwalidi.android.fancup.service

import com.amrwalidi.android.fancup.service.model.UserDoc

interface UserService {
    suspend fun createUser(id: String, username: String, email: String)
    suspend fun getUserById(id: String): UserDoc?
    suspend fun getUserByUsername(username: String): UserDoc?
    suspend fun getUserByEmail(email: String) :  UserDoc?
    suspend fun deleteAccount(id: String)
}