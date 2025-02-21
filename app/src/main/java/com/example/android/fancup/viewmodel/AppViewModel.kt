package com.example.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.fancup.database.getDatabase
import com.example.android.fancup.domain.User
import com.example.android.fancup.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = AuthenticationRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    private val _pageName = MutableLiveData("Home")
    val pageName: LiveData<String>
        get() = _pageName

    private val _selectedPage = MutableLiveData(arrayOf(false, false, true, false, false))
    val selectedPage: LiveData<Array<Boolean>>
        get() = _selectedPage

    private val _transitionDirection = MutableLiveData<Int>()
    val transitionDirection: LiveData<Int>
        get() = _transitionDirection

    private val _toLoginScreen = MutableLiveData(false)
    val toLoginScreen: LiveData<Boolean>
        get() = _toLoginScreen

    init {
        viewModelScope.launch {
            repo.loggedUser.collect{ u ->
                if (u == null) returnToLoginScreen()
            }
            _user.value = repo.getUser()
        }
    }

    fun navigate(newPage: Int) {
        if (newPage - _page.value!! > 0) {
            _transitionDirection.value = 1
        } else {
            _transitionDirection.value = -1
        }
        _page.value = newPage
        _selectedPage.value = Array(5) { false }.apply {
            this[newPage - 1] = true
        }
        _pageName.value = when (newPage) {
            1 -> "Store"
            2 -> "Champions"
            3 -> "Home"
            4 -> "Challenges"
            5 -> "Settings"
            else -> return
        }
    }

    private fun returnToLoginScreen() {
        _toLoginScreen.value = true
    }

    fun resetToLoginScreen() {
        _toLoginScreen.value = false
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}