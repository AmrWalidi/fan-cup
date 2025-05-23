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
import com.amrwalidi.android.fancup.repository.NotificationRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository(database)
    private val userRepo = UserRepository(database)
    private val notificationRepo = NotificationRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _page = MutableLiveData(3)
    val page: LiveData<Int>
        get() = _page

    private val _selectedPage = MutableLiveData(arrayOf(false, false, true, false, false))
    val selectedPage: LiveData<Array<Boolean>>
        get() = _selectedPage

    private val _transitionDirection = MutableLiveData<Int>()
    val transitionDirection: LiveData<Int>
        get() = _transitionDirection

    private val _toLoginScreen = MutableLiveData(false)
    val toLoginScreen: LiveData<Boolean>
        get() = _toLoginScreen

    private val _notificationList = MutableLiveData<List<Int>>(listOf())
    val notificationList: LiveData<List<Int>>
        get() = _notificationList

    init {
        viewModelScope.launch {
            launch {
                authRepo.loggedUser.collect { u ->
                    if (u == null) returnToLoginScreen()
                }
            }
            _user.value = userRepo.getUser()
            userRepo.updateRank()
        }
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            if(_user.value != null){
                _notificationList.value = notificationRepo.fetchNotifications(_user.value!!.id)
            }
        }
    }

    fun formatInt(number: Int): String {
        return "%,d".format(number)
    }

    fun navigate(newPage: Int) {
        if (newPage - _page.value!! > 0) {
            _transitionDirection.value = 1
        } else if (newPage - _page.value!! < 0) {
            _transitionDirection.value = -1
        }

        if (newPage != _page.value) {
            _page.value = newPage
        }
        if (newPage <= 5) {
            _selectedPage.value = Array(5) { false }.apply {
                this[newPage - 1] = true
            }
        }

    }

    private fun returnToLoginScreen() {
        _toLoginScreen.value = true
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