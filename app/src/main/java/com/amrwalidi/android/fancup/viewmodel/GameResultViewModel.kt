package com.amrwalidi.android.fancup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.domain.Question

class GameResultViewModel(
    private val questionViewModel: QuestionViewModel,
    private val questionId: Long,
    private val questionList: ArrayList<Question>,
    application: Application
) :
    AndroidViewModel(application) {

    private var _time: Long = questionViewModel.reachedTime
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

    private val _nextQuestion = MutableLiveData(-1L)
    val nextQuestion: LiveData<Long>
        get() = _nextQuestion

    init {
        searchNextQuestion()
    }

    fun nextQuestion() {
        _toNextQuestion.value = true
    }

    fun toMenu() {
        _toMenu.value = true
    }

    private fun searchNextQuestion() {
        val idx = questionList.find { it.id == questionId }?.listId?.toInt()?.plus(1)
        _nextQuestion.value = questionList.find { it.listId.toInt() == idx }?.id
    }

    class Factory(
        val viewModel: QuestionViewModel,
        val id: Long,
        val list: ArrayList<Question>,
        val app: Application
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameResultViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameResultViewModel(viewModel, id, list, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}