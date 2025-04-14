package com.amrwalidi.android.fancup.service

import com.amrwalidi.android.fancup.service.model.QuestionDoc
import kotlinx.coroutines.flow.Flow

interface QuestionService {
    suspend fun getQuestions(): List<QuestionDoc?>
    suspend fun getQuestionById(id: String): QuestionDoc?
    suspend fun createUserQuestionRef(userId: String, questionId: String)
    suspend fun getUserQuestions(userId: String): List<QuestionDoc?>
    suspend fun updateStars(userId: String, questionId: String, stars: Int): Flow<Response>
    suspend fun updatePlayability(
        userId: String,
        questionId: String
    ): Flow<Response>
    suspend fun deleteUserQuestions(id: String): Flow<Response>
}