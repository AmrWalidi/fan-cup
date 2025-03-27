package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.repository.QuestionRepository
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application, questionId: Long) :
    AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = QuestionRepository(database)

    private var startingTime = 31000L
    private var reachedTime = 0L
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _timeRemaining = MutableLiveData<String>()
    val timeRemaining: LiveData<String> get() = _timeRemaining

    private val _showPopup = MutableLiveData(false)
    val showPopup: LiveData<Boolean>
        get() = _showPopup

    private var _hasExitGamePopup = false
    val hasExitGamePopup: Boolean
        get() = _hasExitGamePopup

    private val _hasExitGame = MutableLiveData(false)
    val hasExitGame: LiveData<Boolean>
        get() = _hasExitGame

    private val _hearts = MutableLiveData(2)
    val hearts: LiveData<Int>
        get() = _hearts

    private val _deletedHearts = MutableLiveData(0)
    val deletedHearts: LiveData<Int>
        get() = _deletedHearts

    private var countDownTimer: CountDownTimer? = null

    init {
        viewModelScope.launch {
            _question.value = repo.getQuestionsById(questionId)
            startCountdown()
        }
    }

    private fun startCountdown() {

        countDownTimer = object : CountDownTimer(startingTime, 1000L) {
            override fun onTick(millisUntilFinished: Long) {

                _timeRemaining.value = if (millisUntilFinished < 10000)
                    "00:0${millisUntilFinished / 1000}"
                else
                    "00:${millisUntilFinished / 1000}"

                reachedTime = millisUntilFinished
            }

            override fun onFinish() {
                _timeRemaining.value = "00:00"
            }
        }

        countDownTimer?.start()
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }

    fun exitGamePopup() {
        _hasExitGamePopup = true
        _showPopup.value = true
    }

    fun dismissPopup() {
        _hasExitGamePopup = false
        _showPopup.value = false
    }

    fun exitGame() {
        _hasExitGame.value = true
    }

    fun wrongAnswer() {
        if (_deletedHearts.value!! < _hearts.value!!) {
            _deletedHearts.value = _deletedHearts.value!! + 1
        }
    }

    fun extraTime() {
        startingTime = reachedTime + 10000L
        startCountdown()
    }

    fun extraHeart(){
//        _deletedHearts.value = _deletedHearts.value!! - 1
    }


    class Factory(val app: Application, val id: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QuestionViewModel(app, id) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}