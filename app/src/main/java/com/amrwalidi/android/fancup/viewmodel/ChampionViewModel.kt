package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChampionViewModel(application: Application) : AndroidViewModel(application) {

    private val _toPlayWithFriendPage = MutableLiveData<Boolean>()
    val toPlayWithFriendPage: LiveData<Boolean>
        get() = _toPlayWithFriendPage

    fun navigateToPlayWithFriendPage() {
        _toPlayWithFriendPage.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChampionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChampionViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}