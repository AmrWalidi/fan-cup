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
import kotlinx.coroutines.launch

class InvitedGameViewModel(opponentId: String, application: Application) :
    AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val matchRepo = MatchRepository(database)
    private val userRepo = UserRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _opponent = MutableLiveData<User>()
    val opponent: LiveData<User>
        get() = _opponent

    private var _match = ""
    val match: String
        get() = _match

    private var _question: String = ""
    val question: String
        get() = _question

    private val _isReady = MutableLiveData(false)
    val isReady: LiveData<Boolean>
        get() = _isReady


    init {
        viewModelScope.launch {
            _user.value = userRepo.getUser()
            val user = userRepo.getUser()
            _match = matchRepo.createInvitedMatch(user?.id ?: "", opponentId)
            _opponent.value = userRepo.getUserById(opponentId)
            _question = matchRepo.getMatchQuestion(_match)
            while (_isReady.value == false) {
                _isReady.value = matchRepo.playersReady(_match)
            }
        }

    }

    class Factory(private val opponentId: String, val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InvitedGameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InvitedGameViewModel(opponentId, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}