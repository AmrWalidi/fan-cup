package com.example.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.fancup.database.getDatabase
import com.example.android.fancup.repository.AuthenticationRepository
import com.example.android.fancup.service.impl.AuthenticationServiceImpl

class AuthenticationViewModel(application: Application) : AndroidViewModel(application){
    private val database = getDatabase(application)
    private val authService = AuthenticationServiceImpl()
    private val repo = AuthenticationRepository(database, authService)

    fun signIn() {

    }

    fun toRegisterPage() {
    }

    fun toSignInPage() {

    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return AuthenticationViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}