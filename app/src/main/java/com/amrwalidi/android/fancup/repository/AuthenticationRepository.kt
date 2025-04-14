package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.impl.AuthenticationServiceImpl
import com.amrwalidi.android.fancup.service.impl.QuestionServiceImpl
import com.amrwalidi.android.fancup.service.impl.UserServiceImpl
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthenticationRepository(val database: FanCupDatabase) {
    private val authService = AuthenticationServiceImpl()
    private val userService = UserServiceImpl()
    private val questionService = QuestionServiceImpl()

    val loggedUser: Flow<FirebaseUser?>
        get() = authService.currentUser


    fun hasUser(): Boolean {
        return authService.hasUser()
    }

    suspend fun resetPassword(email: String) {
        authService.resetPassword(email)
    }

    suspend fun signIn(email: String, password: String): Flow<Response> =
        authService.signIn(email, password)


    suspend fun register(email: String, password: String): Flow<Response> =
        authService.register(email, password)


    suspend fun signOut() {
        authService.signOut()
        database.clearAllData()
    }

    suspend fun deleteAccount(id: String) {
        authService.deleteAccount()
        userService.deleteUser(id).collect { res1 ->
            if (res1 is Response.Success)
                questionService.deleteUserQuestions(id).collect { res2 ->
                    if (res2 is Response.Success)
                        database.clearAllData()
                }
        }
    }
}