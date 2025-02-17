package com.example.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    private val _pageName = MutableLiveData("Home")
    val pageName: LiveData<String>
        get() = _pageName

    private val _selectedPage = MutableLiveData(arrayOf(true, false, false, false, false))
    val selectedPage: LiveData<Array<Boolean>>
        get() = _selectedPage


    fun navigate(page: Int) {
        _page.value = page
        _selectedPage.value = Array(5) { false }.apply {
            this[page - 1] = true
        }
        _pageName.value = when (page) {
            1 -> "Home"
            2 -> "Challenges"
            3 -> "Champions"
            4 -> "Settings"
            5 -> "Store"
            else -> return
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}