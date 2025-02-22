package com.example.android.fancup.repository

import com.example.android.fancup.database.FanCupDatabase
import com.example.android.fancup.database.entity.asDomainUser
import com.example.android.fancup.domain.User
import com.example.android.fancup.service.Response
import com.example.android.fancup.service.impl.AuthenticationServiceImpl
import com.example.android.fancup.service.impl.UserServiceImpl
import com.example.android.fancup.service.model.asDatabaseUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class AuthenticationRepository(private val database: FanCupDatabase) {
    private val authService = AuthenticationServiceImpl()
    private val userService = UserServiceImpl()

    val loggedUser: Flow<FirebaseUser?>
        get() = authService.currentUser

    suspend fun getUser(): User? {
        val u =  database.userDao.getUser().asDomainUser()
        return u
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

    fun signIn(email: String, password: String): Flow<Response> = flow {
        authService.signIn(email, password).collect { res ->
            if (res is Response.Success) {
                val user = userService.getUserById(res.data.toString()).asDatabaseUser()
                user?.let { database.userDao.insert(it) }
            }
            emit(res)
        }

    }

    fun register(username: String, email: String, password: String): Flow<Response> = flow {
        val existingUser = userService.getUserByUsername(username)
        if (existingUser != null) {
            emit(Response.Failure(Exception("This username has been taken")))
            return@flow
        }

        authService.register(email, password).collect { res ->
            if (res is Response.Success) {
                userService.createUser(res.data.toString(), username, email)
            }
            emit(res)
        }
    }

    suspend fun signOut() {
        authService.signOut()
        database.userDao.delete()
    }
}