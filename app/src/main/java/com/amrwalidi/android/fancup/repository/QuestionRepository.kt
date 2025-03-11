package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.database.entity.QuestionCategoryCrossRef
import com.amrwalidi.android.fancup.database.entity.asDomainQuestion
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.service.impl.QuestionServiceImpl
import com.amrwalidi.android.fancup.service.model.asDatabaseQuestion
import kotlinx.coroutines.coroutineScope

class QuestionRepository(private val database: FanCupDatabase) {

    private val questionService = QuestionServiceImpl()

    suspend fun fetchQuestions() = coroutineScope {
        val fetchedQuestions = questionService.getQuestions()
        fetchedQuestions.map { question ->
            val databaseQuestion = question.asDatabaseQuestion()
            val questionId = database.questionDao.insert(databaseQuestion!!)

            question?.categories?.forEach { categoryId ->
                database.questionDao.insertQuestionCategoryCrossRef(
                    QuestionCategoryCrossRef(
                        questionId,
                        categoryId
                    )
                )
            }

        }
    }

    suspend fun getQuestionsByCategory(id: Int): List<Question>? {
        return database.questionDao.getQuestionsByCategory(id).asDomainQuestion()
    }
}