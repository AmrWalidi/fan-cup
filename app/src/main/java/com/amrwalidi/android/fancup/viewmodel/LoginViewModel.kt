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
import com.amrwalidi.android.fancup.repository.CategoryRepository
import com.amrwalidi.android.fancup.repository.QuestionRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import com.amrwalidi.android.fancup.service.Response
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository()
    private val userRepo = UserRepository(database)
    private val categoryRepo = CategoryRepository(database)
    private val questionRepo = QuestionRepository(database)

    private val _registerPage = MutableLiveData(false)
    val registerPage: LiveData<Boolean>
        get() = _registerPage

    private val _forgetPasswordPage = MutableLiveData(false)
    val forgetPasswordPage: LiveData<Boolean>
        get() = _forgetPasswordPage

    private val _appPage = MutableLiveData(false)
    val appPage: LiveData<Boolean>
        get() = _appPage

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val emailInput = MutableLiveData("")
    val passwordInput = MutableLiveData("")


    fun signIn() {
        viewModelScope.launch {
            val emailValue = emailInput.value
            val passwordValue = passwordInput.value

            if (!emailValue.isNullOrEmpty() && !passwordValue.isNullOrEmpty()) {
                authRepo.signIn(emailValue, passwordValue).collect { res ->
                    when (res) {
                        is Response.Failure -> {
                            _isLoading.value = false
                            _errorMessage.value = res.e.message
                        }

                        is Response.Success -> {
                            _isLoading.value = false
                            userRepo.setUser(res.data.toString())
                            categoryRepo.fetchCategories()
                            questionRepo.fetchQuestions()
                            toAppPage()
                        }

                        is Response.Loading -> _isLoading.value = true

                    }
                }

            } else {
                _errorMessage.value = "Fill the Fields"
            }
        }
    }

    private fun toAppPage() {
        _appPage.value = true
    }

    fun toRegisterPage() {
        _registerPage.value = true
    }

    fun resetRegisterPage() {
        _registerPage.value = false
    }

    fun toForgetPasswordPage() {
        _forgetPasswordPage.value = true
    }

    fun resetForgetPasswordPage() {
        _forgetPasswordPage.value = false
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