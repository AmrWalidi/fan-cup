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

    lateinit var questions: List<Question>

    private var page = 1

    private val _firstPage = MutableLiveData(true)
    val firstPage: LiveData<Boolean>
        get() = _firstPage

    private val _selectedQuestion = MutableLiveData<Long>()
    val selectedQuestion: LiveData<Long>
        get() = _selectedQuestion

    private val _lastPage = MutableLiveData(false)
    val lastPage: LiveData<Boolean>
        get() = _lastPage

    private val _displayedQuestions = MutableLiveData<List<Question>>()
    val displayedQuestions: LiveData<List<Question>>
        get() = _displayedQuestions


    init {
        viewModelScope.launch {
            val categoryId = categoryRepo.getSelectedCategory()?.id
            questions = categoryId?.let { questionRepo.getQuestionsByCategory(it) }!!

            if (questions.size < 9) {
                _displayedQuestions.value = questions.subList(0, questions.size)
                _lastPage.value = true
            } else _displayedQuestions.value = questions.subList(0, 9)
        }
    }

    fun nextQuestions() {
        page++
        if (questions.size < page * 9) {
            _displayedQuestions.value = questions.subList((page - 1) * 9, questions.size)
            _lastPage.value = true
        } else {
            _displayedQuestions.value = questions.subList((page - 1) * 9, page * 9)
            _firstPage.value = false
            _lastPage.value = false
        }
    }

    fun prevQuestions() {
        page--
        _displayedQuestions.value = questions.subList((page - 1) * 9, page * 9)
        if (page == 1)
            _firstPage.value = true
        else {
            _lastPage.value = false
            _firstPage.value = false
        }
    }

    fun selectQuestion(id: Long) {
        _selectedQuestion.value = id

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