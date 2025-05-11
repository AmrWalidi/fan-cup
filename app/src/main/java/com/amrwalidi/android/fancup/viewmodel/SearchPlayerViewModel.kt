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
import com.amrwalidi.android.fancup.repository.MatchRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val matchRepo = MatchRepository(database)
    private val userRepo = UserRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private var _match = ""
    val match : String
        get() = _match

    private val _isReady = MutableLiveData(false)
    val isReady: LiveData<Boolean>
        get() = _isReady

    private var _question: String = ""
    val question : String
        get() = _question

    private val _searchedUser = MutableLiveData<User>()
    val searchedUser: LiveData<User>
        get() = _searchedUser

    init {
        viewModelScope.launch {
            _user.value = userRepo.getUser()
            matchRepo.enterLobby(_user.value?.id ?: "")
        }
    }

    fun exitLobby() {
        viewModelScope.launch {
            matchRepo.exitLobby(_user.value?.id ?: "")
        }
    }

    fun searchForPlayer() {

        viewModelScope.launch {
            val playersInLobby = matchRepo.getAllPlayersInLobby()
            delay(playersInLobby * 2000L)
            while (_searchedUser.value == null) {
                val pair = matchRepo.searchForPlayer(_user.value?.id ?: "")
                _searchedUser.value = pair.first
                _match = pair.second
            }

        }
    }

    fun playersReady() {
        viewModelScope.launch {
            _question = matchRepo.getMatchQuestion(_match)
            while (_isReady.value == false) {
                _isReady.value = matchRepo.playersReady(_match)
            }
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