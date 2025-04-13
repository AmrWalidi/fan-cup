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

class QuestionViewModel(
    application: Application,
    val questionId: String,
) :
    AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = QuestionRepository(database)

    private var countDownTimer: CountDownTimer? = null

    private var startingTime = 31000L
    private var _reachedTime = 0L
    val reachedTime: Long
        get() = _reachedTime

    private val _timeRemaining = MutableLiveData<String>()
    val timeRemaining: LiveData<String> get() = _timeRemaining

    private val _clickedHelpers = MutableLiveData(arrayOf(false, false, false))
    val clickedHelpers: LiveData<Array<Boolean>>
        get() = _clickedHelpers

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

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

    private val _completionMessage = MutableLiveData("")
    val completionMessage: LiveData<String>
        get() = _completionMessage

    private var _points = 0
    val points: Int
        get() = _points

    private var _stars = 3
    val stars: Int
        get() = _stars


    init {
        viewModelScope.launch {
            _question.value = repo.getQuestionById(questionId)
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

                _reachedTime = millisUntilFinished

                if (_completionMessage.value?.isNotEmpty() == true) {
                    countDownTimer?.cancel()
                }
            }

            override fun onFinish() {
                _timeRemaining.value = "00:00"
                _completionMessage.value = "Time Out"
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
            _deletedHearts.value = _deletedHearts.value?.plus(1)
        } else {
            _completionMessage.value = "Game Over"
        }
    }

    fun successfulCompletion() {
        _points = 10
        _completionMessage.value = "Congratulation"
    }

    fun calculateStars() {
        if (_clickedHelpers.value?.contains(true) == true)
            _stars--
        if (_deletedHearts.value!! > 0)
            _stars--
        if (_reachedTime < 15)
            _stars--
        if (_stars < 1)
            _stars = 1
        if (_completionMessage.value == "Game Over")
            _stars = 0
    }

    fun extraTime() {
        if (_clickedHelpers.value?.get(2) == false) {
            startingTime = reachedTime + 10000L
            countDownTimer?.cancel()
            startCountdown()
            _clickedHelpers.value?.let { currentArray ->
                val updatedArray = currentArray.copyOf()
                updatedArray[2] = true
                _clickedHelpers.value = updatedArray
            }
        }
    }

    fun extraHeart() {
        if (_clickedHelpers.value?.get(0) == false) {
            if (_deletedHearts.value == 0)
                _hearts.value = _hearts.value?.plus(1)
            else _deletedHearts.value = _deletedHearts.value!! - 1
            _clickedHelpers.value?.let { currentArray ->
                val updatedArray = currentArray.copyOf()
                updatedArray[0] = true
                _clickedHelpers.value = updatedArray
            }
        }
    }


    class Factory(val app: Application, val id: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QuestionViewModel(app, id) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}