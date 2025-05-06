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

class SearchPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = UserRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _searchedUser = MutableLiveData<User>()
    val searchedUser: LiveData<User>
        get() = _searchedUser

    init {
        viewModelScope.launch {
            _user.value = repo.getUser()
            repo.enterLobby()
        }
    }

    fun exitLobby() {
        viewModelScope.launch {
            repo.exitLobby()
        }
    }

    fun searchForPlayer() {
        viewModelScope.launch {
            _searchedUser.value = repo.searchForPlayer()
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchPlayerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchPlayerViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}