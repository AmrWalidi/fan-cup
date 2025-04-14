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

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository(database)
    private val userRepo = UserRepository(database)
    private val categoryRepo = CategoryRepository(database)
    private val questionRepo = QuestionRepository(database)

    val usernameInput = MutableLiveData("")
    val emailInput = MutableLiveData("")
    val passwordInput = MutableLiveData("")
    val repeatedPasswordInput = MutableLiveData("")

    private val _loginPage = MutableLiveData(false)
    val loginPage: LiveData<Boolean>
        get() = _loginPage

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    fun register() {
        viewModelScope.launch {
            val emailValue = emailInput.value
            val passwordValue = passwordInput.value
            val usernameValue = usernameInput.value
            val repeatedPasswordValue = repeatedPasswordInput.value

            if (emailValue.isNullOrEmpty() || passwordValue.isNullOrEmpty() || usernameValue.isNullOrEmpty() || repeatedPasswordValue.isNullOrEmpty()) {
                _errorMessage.value = "Fill the Fields"
                return@launch
            }

            if (passwordValue.length <= 6 || !passwordValue.matches("^(?=.*[A-Za-z])(?=.*\\d).+$".toRegex())) {
                _errorMessage.value =
                    "Password must be between 6 and 25 characters long and must contain at least 1 number, 1 uppercase letter, and one lowercase letter"
                return@launch
            }

            if (passwordValue != repeatedPasswordValue) {
                _errorMessage.value = "Password and repeated password do not match"
                return@launch
            }

            if (userRepo.getUserByUsername(usernameValue) != null){
                _errorMessage.value = "This username has been taken"
                return@launch
            }

            authRepo.register(emailValue, passwordValue).collect { res ->
                when (res) {
                    is Response.Success -> {
                        _isLoading.value = false
                        userRepo.createUser(res.data.toString(), usernameValue, emailValue)
                        categoryRepo.fetchCategories()
                        questionRepo.fetchQuestions(res.data.toString())
                        toLoginPage()
                    }

                    is Response.Failure -> {
                        _isLoading.value = false
                        _errorMessage.value = res.e.message
                    }

                    is Response.Loading -> _isLoading.value = true
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