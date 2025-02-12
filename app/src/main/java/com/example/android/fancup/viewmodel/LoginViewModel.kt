package com.example.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.fancup.database.getDatabase
import com.example.android.fancup.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repo = AuthenticationRepository(database)

    private val _userID = MutableLiveData<String>()
    val userID: LiveData<String>
        get() = _userID

    private val _registerPage = MutableLiveData(false)
    val registerPage : LiveData<Boolean>
        get() = _registerPage

    private val _appPage = MutableLiveData(false)
    val appPage : LiveData<Boolean>
        get() = _appPage

    val emailInput = MutableLiveData("")
    val passwordInput = MutableLiveData("")


    fun signIn() {
        viewModelScope.launch {
            val emailValue = emailInput.value
            val passwordValue = passwordInput.value

            if (!emailValue.isNullOrBlank() && !passwordValue.isNullOrBlank()) {
                _userID.value = repo.signIn(emailValue, passwordValue)
            }

            if (_userID.value != null){
                toAppPage()
            }
        }
    }

    private fun toAppPage() {
        _appPage.value = true
    }

    fun toRegisterPage() {
        _registerPage.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}