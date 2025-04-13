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
import com.amrwalidi.android.fancup.repository.QuestionRepository
import com.amrwalidi.android.fancup.repository.UserRepository
import kotlinx.coroutines.launch

class GameResultViewModel(
    private val questionViewModel: QuestionViewModel,
    private val questionList: ArrayList<Question>,
    application: Application
) :
    AndroidViewModel(application) {

    val database = getDatabase(application)
    private val questionRepo = QuestionRepository(database)
    private val userRepo = UserRepository(database)

    private var _time: Long = questionViewModel.reachedTime / 1000
    val time: Long
        get() = _time

    private var _points: Int = questionViewModel.points
    val points: Int
        get() = _points

    private val _toNextQuestion = MutableLiveData(false)
    val toNextQuestion: LiveData<Boolean>
        get() = _toNextQuestion

    private val _toMenu = MutableLiveData(false)
    val toMenu: LiveData<Boolean>
        get() = _toMenu

    private val _nextQuestion = MutableLiveData<String>()
    val nextQuestion: LiveData<String>
        get() = _nextQuestion

    init {
        searchNextQuestion()
        viewModelScope.launch {
            userRepo.getUser()
                ?.let {
                    questionRepo.updateStars(
                        it.id,
                        questionViewModel.questionId,
                        questionViewModel.stars
                    )
                }
            _nextQuestion.value?.let {
                userRepo.getUser()
                    ?.let { user -> questionRepo.updatePlayability(user.id, it) }
            }

        }
    }

    fun nextQuestion() {
        _toNextQuestion.value = true
    }

    fun toMenu() {
        _toMenu.value = true
    }

    private fun searchNextQuestion() {
        val idx =
            questionList.find { it.id == questionViewModel.questionId }?.listId?.toInt()?.plus(1)
        _nextQuestion.value = questionList.find { it.listId.toInt() == idx }?.id
    }

    class Factory(
        val viewModel: QuestionViewModel,
        val list: ArrayList<Question>,
        val app: Application
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameResultViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameResultViewModel(viewModel, list, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}