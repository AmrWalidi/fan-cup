package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.impl.AuthenticationServiceImpl
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthenticationRepository() {
    private val authService = AuthenticationServiceImpl()

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
    }
}