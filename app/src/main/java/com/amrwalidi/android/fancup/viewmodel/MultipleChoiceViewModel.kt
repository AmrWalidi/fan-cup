package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.domain.Question

class MultipleChoiceViewModel(application: Application, val question: Question?) :
    AndroidViewModel(application) {

    private val _selectedAnswer = MutableLiveData(-1)
    val selectedAnswer: LiveData<Int>
        get() = _selectedAnswer

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private val _wrongAnswer = MutableLiveData(false)
    val wrongAnswer: LiveData<Boolean>
        get() = _wrongAnswer


    fun selectAnswer(index: Int) {
        _selectedAnswer.value = index
        if (question?.options?.get(index) == question?.answers?.get(0)) _message.value =
            "CORRECT ANSWER"
        else {
            _message.value = "WRONG ANSWER"
            _wrongAnswer.value = true
        }
    }

    fun removeWrongAnswer() {
        _wrongAnswer.value = false
    }

    class Factory(val app: Application, val question: Question?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MultipleChoiceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MultipleChoiceViewModel(app, question) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}