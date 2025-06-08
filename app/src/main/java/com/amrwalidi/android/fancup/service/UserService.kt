package com.amrwalidi.android.fancup.service

import android.net.Uri
import com.amrwalidi.android.fancup.service.model.UserDoc
import kotlinx.coroutines.flow.Flow

interface UserService {
    suspend fun createUser(id: String, username: String, email: String)
    suspend fun getUserById(id: String): UserDoc?
    suspend fun getUsers(id: String, username: String): List<UserDoc>?
    suspend fun getUsers(): List<UserDoc>?
    suspend fun getUserByUsername(username: String): UserDoc?
    suspend fun getUserByEmail(email: String): UserDoc?
    suspend fun updatePoints(id: String, points: Int): Flow<Response>
    suspend fun updateCoins(id: String, coins: Int): Flow<Response>
    suspend fun updateLevel(id: String, level: Int): Flow<Response>
    suspend fun updateRank()
    suspend fun deleteUser(id: String): Flow<Response>
    suspend fun getUserProfileImage(id: String): Flow<Response>
    suspend fun uploadUserProfileImage(id: String, image: Uri): Flow<Response>
    suspend fun addFriend(id: String, friend: String): Flow<Response>
    suspend fun getFriends(id: String): Flow<Response>
}