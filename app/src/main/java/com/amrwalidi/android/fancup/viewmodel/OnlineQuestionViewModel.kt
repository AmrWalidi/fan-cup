package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.Match
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.repository.MatchRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class OnlineQuestionViewModel(
    private val matchId: String, application: Application
) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    val userRepo = UserRepository(database)
    private val matchRepo = MatchRepository(database)

    private var _user: User? = null
    val user: User?
        get() = _user

    private var _match: Match? = null
    val match: Match?
        get() = _match

    private val _winner = MutableLiveData<User>()
    val winner: LiveData<User>
        get() = _winner

    init {
        viewModelScope.launch {
            _user = userRepo.getUser()
            _match = matchRepo.getMatch(matchId)
        }
    }

    fun getWinner() {
        viewModelScope.launch {
            val winnerId = matchRepo.getWinner(matchId)
            if (winnerId.isNotEmpty()) {
                _winner.value = userRepo.getUserById(winnerId)
            }

        }
    }

    fun setWinner(winner: String) {
        viewModelScope.launch {
            matchRepo.setWinner(winner, matchId)
        }
    }

    fun updatePoints(points: Int){
        viewModelScope.launch {
            _winner.value?.let {
                if (points > 0) {
                    userRepo.updateCoins(it.id, it.coins + (points / 2))
                }
                userRepo.updatePoints(it.id, points + it.points)
                if (it.points + points == (1000 * it.level.toInt())) {
                    userRepo.updateLevel(it.id, it.level.toInt() + 1)
                    userRepo.updatePoints(it.id,0)
                    userRepo.updateRank()
                }
            }
        }
    }

    fun endMatch() {
        viewModelScope.launch {
            matchRepo.endMatch(matchId)
        }
    }

    class Factory(private val matchId: String, val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OnlineQuestionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OnlineQuestionViewModel(matchId, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}