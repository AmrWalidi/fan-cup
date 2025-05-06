package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.Notification
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.repository.NotificationRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class NotificationViewModel(val type: Int, application: Application) :
    AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val notificationRepo = NotificationRepository(database)
    private val userRepo = UserRepository(database)

    private var user: User? = null

    private val _notifications = MutableLiveData<List<Notification>>(listOf())
    val notifications: LiveData<List<Notification>>
        get() = _notifications

    init {
        viewModelScope.launch {
            user = userRepo.getUser()
            _notifications.value = notificationRepo.getNotifications(type)
            notificationRepo.readNotifications(user?.id ?: "", type)
        }
    }

    fun acceptFriendRequest(friend: String){
        viewModelScope.launch {
            user?.id?.let { id -> notificationRepo.acceptFriendRequest(id , friend) }

        }
    }

    class Factory(val type: Int, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(type, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}