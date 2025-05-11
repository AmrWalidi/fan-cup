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

class ChampionViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = UserRepository(database)

    private val _toPlayWithFriendPage = MutableLiveData<Boolean>()
    val toPlayWithFriendPage: LiveData<Boolean>
        get() = _toPlayWithFriendPage

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    init {
        viewModelScope.launch {
            _users.value = repo.getUsers()?.sortedBy { it.rank }
        }
    }

    fun navigateToPlayWithFriendPage() {
        _toPlayWithFriendPage.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChampionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChampionViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}