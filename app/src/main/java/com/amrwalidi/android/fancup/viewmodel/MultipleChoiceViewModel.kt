package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.domain.Question

class MultipleChoiceViewModel(val lang: String, val question: Question?, application: Application) :
    AndroidViewModel(application) {

    private val _selectedAnswer = MutableLiveData(-1)
    val selectedAnswer: LiveData<Int>
        get() = _selectedAnswer

    private val _correctAnswer = MutableLiveData(false)
    val correctAnswer: LiveData<Boolean>
        get() = _correctAnswer

    private val _wrongAnswer = MutableLiveData(false)
    val wrongAnswer: LiveData<Boolean>
        get() = _wrongAnswer


    fun selectAnswer(index: Int) {
        _selectedAnswer.value = index
        if (question?.options?.get(index) == question?.answers?.get(0)) _correctAnswer.value = true
        else {
            _wrongAnswer.value = true
        }
    }

    fun removeWrongAnswer() {
        _wrongAnswer.value = false
    }

    class Factory(val lang: String, val question: Question?, val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MultipleChoiceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MultipleChoiceViewModel(lang, question, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}