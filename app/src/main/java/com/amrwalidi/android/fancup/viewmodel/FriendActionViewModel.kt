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
import com.amrwalidi.android.fancup.repository.NotificationRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class FriendActionViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)
    private val userRepo = UserRepository(database)
    private val notificationRepo = NotificationRepository(database)

    private val _users = MutableLiveData(listOf<User>())
    val users: LiveData<List<User>>
        get() = _users

    private val _friends = MutableLiveData(listOf<User>())
    val friends: LiveData<List<User>>
        get() = _friends


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _notificationMessage = MutableLiveData("")
    val notificationMessage: LiveData<String>
        get() = _notificationMessage

    private val _invitation =
        MutableLiveData<Map<String, Any>>(mapOf("invitee" to "", "enteredMatch" to false))
    val invitation: LiveData<Map<String, Any>>
        get() = _invitation

    val searchedUser = MutableLiveData("")

    fun onSearchTextChanged(username: String) {
        searchedUser.value = username
    }

    fun getFriends(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _friends.value = userRepo.getFriends(username)
            _isLoading.value = false
        }
    }

    fun getUsers(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _users.value = userRepo.getUsers(username)
            _isLoading.value = false
        }
    }

    fun sendNotification(sender: String, receiver: String, message: String, type: Int) {
        viewModelScope.launch {
            _notificationMessage.value =
                notificationRepo.sendNotification(sender, receiver, message, type)
            if (type == 2) {
                _invitation.value = mapOf("invitee" to receiver, "enteredMatch" to true)
            }
        }
    }

    fun resetMessage() {
        _notificationMessage.value = ""
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendActionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendActionViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}