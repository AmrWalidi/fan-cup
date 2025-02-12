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

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repo = AuthenticationRepository(database)

    val usernameInput = MutableLiveData("")
    val emailInput = MutableLiveData("")
    val passwordInput = MutableLiveData("")
    val repeatedPasswordInput = MutableLiveData("")

    private val _loginPage = MutableLiveData(false)
    val loginPage: LiveData<Boolean>
        get() = _loginPage

    fun register() {
        viewModelScope.launch {
            val emailValue = emailInput.value
            val passwordValue = passwordInput.value
            val usernameValue = usernameInput.value
            val repeatedPasswordValue = repeatedPasswordInput.value

            if (!emailValue.isNullOrBlank() && !passwordValue.isNullOrBlank() && !usernameValue.isNullOrBlank() && !repeatedPasswordValue.isNullOrBlank()) {
                if (passwordValue.length > 6 && passwordValue.matches("^(?=.*[A-Za-z])(?=.*\\d).+$".toRegex())) {
                    if (passwordValue == repeatedPasswordValue) {
                        val isRegistered = repo.register(usernameValue, emailValue, passwordValue)
                        if (isRegistered) toLoginPage()
                    }
                }
            }
        }
    }

    private fun toLoginPage() {
        _loginPage.value = true
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}