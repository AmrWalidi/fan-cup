package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrwalidi.android.fancup.database.getDatabase
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.repository.CategoryRepository
import com.amrwalidi.android.fancup.repository.QuestionRepository
import kotlinx.coroutines.launch

class GameLevelViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val questionRepo = QuestionRepository(database)
    private val categoryRepo = CategoryRepository(database)

    private val _question = MutableLiveData<List<Question>>()
    val question: LiveData<List<Question>>
        get() = _question


    init {
        viewModelScope.launch {
            val categoryId = categoryRepo.getSelectedCategory()?.id
            _question.value = categoryId?.let { questionRepo.getQuestionsByCategory(it) }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameLevelViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameLevelViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}