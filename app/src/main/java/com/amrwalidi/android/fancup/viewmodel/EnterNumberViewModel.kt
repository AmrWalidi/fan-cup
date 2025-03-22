package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.domain.Question

class EnterNumberViewModel(application: Application, val question: Question?) :
    AndroidViewModel(application) {

    val answer = MutableLiveData("")

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private val _wrongAnswer = MutableLiveData(false)
    val wrongAnswer: LiveData<Boolean>
        get() = _wrongAnswer

    fun submit(){
        if (answer.value == question?.answers?.get(0)) _message.value = "CORRECT ANSWER"
        else {
            _message.value = "WRONG ANSWER"
            _wrongAnswer.value = true
        }
    }

    fun removeWrongAnswer(){
        _wrongAnswer.value = false
    }

    class Factory(val app: Application, val question: Question?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EnterNumberViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EnterNumberViewModel(app, question) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}