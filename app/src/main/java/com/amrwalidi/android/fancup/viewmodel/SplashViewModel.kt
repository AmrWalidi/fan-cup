package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.repository.AuthenticationRepository

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = AuthenticationRepository(database)

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