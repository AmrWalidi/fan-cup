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

class FriendActionViewModel(application: Application) : AndroidViewModel(application) {

    val database = getDatabase(application)
    private val repo = UserRepository(database)

    private val _users = MutableLiveData(listOf<User>())
    val user: LiveData<List<User>>
        get() = _users

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val searchedUser = MutableLiveData("")


    fun getUsers(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _users.value = repo.getUsers(username)
            _isLoading.value = false
        }
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