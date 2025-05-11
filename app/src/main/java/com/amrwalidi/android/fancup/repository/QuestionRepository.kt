package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.database.entity.QuestionCategoryCrossRef
import com.amrwalidi.android.fancup.database.entity.asDomainQuestion
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.impl.QuestionServiceImpl
import com.amrwalidi.android.fancup.service.model.asDatabaseQuestion
import kotlinx.coroutines.coroutineScope

class QuestionRepository(private val database: FanCupDatabase) {

    private val questionService = QuestionServiceImpl()

    suspend fun fetchQuestions(userId: String) = coroutineScope {
        val fetchedQuestions = questionService.getQuestions()
        fetchedQuestions.map { question ->
            question?.id?.let { questionService.createUserQuestionRef(userId, it) }
        }
    }

    suspend fun retrieveUserQuestions(userId: String) = coroutineScope {
        val userQuestions = questionService.getUserQuestions(userId)
        userQuestions.map { question ->
            val databaseQuestion = question.asDatabaseQuestion()
            database.questionDao.insert(databaseQuestion!!)
            question?.categories?.forEach { categoryId ->
                database.questionDao.insertQuestionCategoryCrossRef(
                    QuestionCategoryCrossRef(
                        question.id,
                        categoryId
                    )
                )
            }
        }
    }

    suspend fun getQuestionsByCategory(id: Int): List<Question>? {
        return database.questionDao.getQuestionsByCategory(id).asDomainQuestion()
    }

    suspend fun getQuestionById(id: String): Question? {
        val databaseQuestion = database.questionDao.getQuestionById(id)
        return databaseQuestion.asDomainQuestion()
    }

    suspend fun updatePlayability(userId: String, questionId: String) {
        questionService.updatePlayability(userId, questionId).collect { res ->
            if (res is Response.Success) {
                database.questionDao.updatePlayability(questionId)
            }
        }
    }

    suspend fun updateStars(userId: String, questionId: String, stars: Int) {
        questionService.updateStars(userId, questionId, stars).collect { res ->
            if (res is Response.Success) {
                database.questionDao.updateStars(questionId, stars)
            }
        }
    }
}