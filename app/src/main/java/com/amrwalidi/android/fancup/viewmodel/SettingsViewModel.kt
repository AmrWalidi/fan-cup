package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.repository.AuthenticationRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository(database)
    private val userRepo = UserRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _popup = MutableLiveData<Int>()
    val popup: LiveData<Int>
        get() = _popup

    init {
        viewModelScope.launch {
            _user.value = userRepo.getUser()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepo.signOut()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            user.value?.let { authRepo.deleteAccount(it.id) }
            database.clearAllData()
        }
    }

    fun showPopUp(number: Int) {
        _popup.value = number
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}