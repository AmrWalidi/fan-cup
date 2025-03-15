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

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _timeRemaining = MutableLiveData<String>()
    val timeRemaining: LiveData<String> get() = _timeRemaining

    private var countDownTimer: CountDownTimer? = null

    init {
        viewModelScope.launch {
            _question.value = repo.getQuestionsById(questionId)
            startCountdown()
        }
    }

    private fun startCountdown() {

        countDownTimer = object : CountDownTimer(31000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished < 10000L) _timeRemaining.value = "00:0${millisUntilFinished / 1000}"
                _timeRemaining.value = "00:${millisUntilFinished / 1000}"
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