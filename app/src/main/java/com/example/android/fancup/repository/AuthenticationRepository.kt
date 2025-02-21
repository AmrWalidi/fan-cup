package com.example.android.fancup.repository

import com.example.android.fancup.database.FanCupDatabase
import com.example.android.fancup.database.entity.asDomainUser
import com.example.android.fancup.domain.User
import com.example.android.fancup.service.impl.AuthenticationServiceImpl
import com.example.android.fancup.service.impl.UserServiceImpl
import com.example.android.fancup.service.model.UserDoc
import com.example.android.fancup.service.model.asDatabaseUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthenticationRepository(private val database: FanCupDatabase) {
    private val authService = AuthenticationServiceImpl()
    private val userService = UserServiceImpl()

    val loggedUser: Flow<FirebaseUser?>
        get() = authService.currentUser

    suspend fun getUser(): User? {
        return database.userDao.getUser().asDomainUser()
    }

    fun hasUser(): Boolean {
        return authService.hasUser()
    }

    suspend fun resetPassword(email: String): Boolean {
        var success = false
        val user = userService.getUserByEmail(email)
        if (user != null) {
            success = true
            authService.resetPassword(email)
        }
        return success
    }

    suspend fun signIn(email: String, password: String): String? {
        val userId = authService.signIn(email, password)?.uid
        if (userId != null) {
            val user = userService.getUserById(userId).asDatabaseUser()
            user?.apply { database.userDao.insert(this) }
        }
        return userId
    }

    suspend fun register(username: String, email: String, password: String): Boolean {
        var loggedIn = 1
        var user: UserDoc? = userService.getUserByEmail(email)
        if (user == null) loggedIn = 0


        user = userService.getUserByUsername(username)
        if (user == null) loggedIn = 0


        if (loggedIn == 1) {
            val userId = authService.register(email, password)
            if (!userId.isNullOrEmpty())
                userService.createUser(userId, username, email)
            return true
        }
        return false

    }

    suspend fun signOut() {
        authService.signOut()
        database.userDao.delete()
    }
}