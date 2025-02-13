package com.example.android.fancup.repository

import com.example.android.fancup.database.FanCupDatabase
import com.example.android.fancup.service.impl.AuthenticationServiceImpl
import com.example.android.fancup.service.impl.UserServiceImpl
import com.example.android.fancup.service.model.asDatabaseUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthenticationRepository(private val database: FanCupDatabase) {
    private val authService = AuthenticationServiceImpl()
    private val userService = UserServiceImpl()

    val loggedUser: Flow<FirebaseUser?>
        get() = authService.currentUser


    fun hasUser(): Boolean {
        return authService.hasUser()
    }

    suspend fun signIn(email: String, password: String): String? {
        return authService.signIn(email, password)?.uid
    }

    suspend fun register(username: String, email: String, password: String): Boolean {
        var loggedIn = 1
        userService.getUserByUsername(username) { user ->
            if (user == null) {
                loggedIn = 0
            }

        }

        userService.getUserByEmail(email) { user ->
            if (user == null) {
                loggedIn = 0
            }

        }
        if (loggedIn == 1) {
            val userId = authService.register(email, password)
            if (!userId.isNullOrEmpty()) {
                userService.createUser(userId, username, email)
                val user = userService.getUserById(userId)
                user?.asDatabaseUser()?.let { userDB ->
                    database.userDao.insert(userDB)
                }
            }
            return true
        }
        return false

    }
}