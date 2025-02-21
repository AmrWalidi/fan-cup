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

class ForgetPasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repo = AuthenticationRepository(database)

    val emailInput = MutableLiveData("")

    private val _loginPage = MutableLiveData(false)
    val loginPage: LiveData<Boolean>
        get() = _loginPage

    fun resetPassword() {
        viewModelScope.launch {
            val success = emailInput.value?.let { repo.resetPassword(it) }
            if (success == true) toLoginPage()
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