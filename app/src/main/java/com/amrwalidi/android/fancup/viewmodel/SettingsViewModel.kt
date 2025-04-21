package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.repository.AuthenticationRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(lang: String, application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val authRepo = AuthenticationRepository(database)
    private val userRepo = UserRepository(database)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _popup = MutableLiveData<Int>()
    val popup: LiveData<Int>
        get() = _popup

    private val _languageList = MutableLiveData(arrayOf(false, false, false))
    val languageList: LiveData<Array<Boolean>>
        get() = _languageList

    init {
        viewModelScope.launch {
            _user.value = userRepo.getUser()
            _languageList.value = Array(3) { false }.apply {
                when (lang) {
                    "ar" -> this[0] = true
                    "en" -> this[1] = true
                    "tr" -> this[2] = true
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepo.signOut()
        }
    }

    fun deleteAccount(password: String) {
        viewModelScope.launch {
            user.value?.let {
                authRepo.deleteAccount(it.id, password)
            }
            database.clearAllData()
        }
    }

    fun showPopUp(number: Int) {
        _popup.value = number
    }

    fun selectLanguage(language: Int) {
        _languageList.value = Array(3) { false }.apply {
            this[language] = true
        }
    }

    fun uploadImage(context: Context, image: Uri) {
        viewModelScope.launch {
            _user.value?.let { userRepo.uploadImage(context, it.id, image) }
            _user.value = userRepo.getUser()
        }
    }


    class Factory(val lang: String, val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(lang, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}