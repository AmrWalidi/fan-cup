package com.amrwalidi.android.fancup.service.impl

import com.amrwalidi.android.fancup.service.QuestionService
import com.amrwalidi.android.fancup.service.model.QuestionDoc
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class QuestionServiceImpl @Inject constructor() : QuestionService {

    private val questionRef = FirebaseFirestore.getInstance().collection("Questions")
    override suspend fun getQuestions(): List<QuestionDoc?> {
        return suspendCancellableCoroutine { continuation ->
            questionRef.get().addOnCompleteListener { task ->
                if (continuation.isActive) {
                    val questions = mutableListOf<QuestionDoc>()
                    task.result.forEach { question ->
                        questions.add((question.toObject(QuestionDoc::class.java)))
                    }
                    continuation.resumeWith(Result.success(questions))
                }
            }.addOnFailureListener { e ->
                if (continuation.isActive) {
                    println("Unable to retrieve questions data")
                    continuation.resumeWithException(e)
                }
            }
        }
    }
}