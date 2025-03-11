package com.amrwalidi.android.fancup.service

import com.amrwalidi.android.fancup.service.model.QuestionDoc

interface QuestionService {
    suspend fun getQuestions() : List<QuestionDoc?>
}