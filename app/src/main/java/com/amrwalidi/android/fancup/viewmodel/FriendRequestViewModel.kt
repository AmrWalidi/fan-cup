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
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class FriendRequestViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)
    private val repo = UserRepository(database)

    private val _users = MutableLiveData(listOf<User>())
    val user: LiveData<List<User>>
        get() = _users

    val searchedUser = MutableLiveData("")


    fun getUsers(username: String) {
        viewModelScope.launch {
            _users.value = repo.getUsers(username)
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendRequestViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendRequestViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}