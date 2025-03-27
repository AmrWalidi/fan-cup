package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.domain.Question

class EnterAnswersViewModel(application: Application, val question: Question?) :
    AndroidViewModel(application) {

    val answer = MutableLiveData("")

    private val _answerList = MutableLiveData<MutableList<String>>(mutableListOf())
    val answerList: LiveData<MutableList<String>>
        get() = _answerList

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private val _wrongAnswer = MutableLiveData(false)
    val wrongAnswer: LiveData<Boolean>
        get() = _wrongAnswer

    fun addAnswer() {
        if (question?.answers?.contains(answer.value) == true) {
            if (_answerList.value?.contains(answer.value) == true) {
                _message.value = "ALREADY ADDED ANSWER"
            } else {
                val currentList = _answerList.value ?: mutableListOf()
                answer.value?.let { currentList.add(it) }
                _answerList.value = currentList
                if (_answerList.value?.size == question.answers.size) {
                    _message.value = "AWESOME!, ALL ANSWERS HAVE BEEN ADDED"
                } else
                    _message.value = "CORRECT ANSWER"
            }
        } else {
            _message.value = "WRONG ANSWER"
            _wrongAnswer.value = true
        }
    }

    fun removeWrongAnswer() {
        _wrongAnswer.value = false
    }

    class Factory(val app: Application, val question: Question?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EnterAnswersViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EnterAnswersViewModel(app, question) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}