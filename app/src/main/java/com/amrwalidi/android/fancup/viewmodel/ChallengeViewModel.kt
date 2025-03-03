package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.Category
import com.amrwalidi.android.fancup.repository.CategoryRepository
import kotlinx.coroutines.launch

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = CategoryRepository(database)

    private val _categories = MutableLiveData<List<Category>>()
    val categories : LiveData<List<Category>>
        get() = _categories

    init {
        viewModelScope.launch {
            _categories.value = repo.getCategories()
        }
    }
    fun selectChallenge(id: Int) {

    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChallengeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChallengeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}