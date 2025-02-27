package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.repository.AuthenticationRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository()
    private val userRepo = UserRepository(database)

    val emailInput = MutableLiveData("")

    private val _loginPage = MutableLiveData(false)
    val loginPage: LiveData<Boolean>
        get() = _loginPage

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun resetPassword() {
        viewModelScope.launch {
            emailInput.value?.let {
                if (userRepo.getUserByEmail(it) != null) {
                    authRepo.resetPassword(it)
                    toLoginPage()
                } else {
                    _errorMessage.value = "This email is not valid"
                }
            }
        }
    }

    private fun toLoginPage() {
        _loginPage.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ForgetPasswordViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}