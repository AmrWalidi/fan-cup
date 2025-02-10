package com.example.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.fancup.database.getDatabase
import com.example.android.fancup.repository.AuthenticationRepository
import com.example.android.fancup.service.impl.AuthenticationServiceImpl

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val authService = AuthenticationServiceImpl()
    private val repo = AuthenticationRepository(database, authService)

    private val _hasUser = MutableLiveData(false)
    val hasUser: LiveData<Boolean>
        get() = _hasUser

    init {
        _hasUser.value = repo.hasUser()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel ")
        }
    }
}