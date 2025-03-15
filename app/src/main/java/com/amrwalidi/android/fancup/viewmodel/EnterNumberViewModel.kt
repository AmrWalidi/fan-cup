package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.domain.Question

class EnterNumberViewModel(application: Application, question: Question?) :
    AndroidViewModel(application) {

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