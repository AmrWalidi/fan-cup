package com.amrwalidi.android.fancup.service.impl

import com.amrwalidi.android.fancup.service.QuestionService
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.model.QuestionDoc
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class QuestionServiceImpl @Inject constructor() : QuestionService {

    private val questionRef = FirebaseFirestore.getInstance().collection("Questions")
    private val userQuestionRef = FirebaseFirestore.getInstance().collection("user-question")
    override suspend fun getQuestions(): List<QuestionDoc?> {
        return suspendCancellableCoroutine { continuation ->
            questionRef.get().addOnCompleteListener { task ->
                if (continuation.isActive) {
                    val questions = mutableListOf<QuestionDoc>()
                    task.result.forEach { doc ->
                        val question = doc.toObject(QuestionDoc::class.java)
                        question.id = doc.id
                        questions.add(question)
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

    override suspend fun getQuestionById(id: String): QuestionDoc? {
        return suspendCancellableCoroutine { continuation ->
            questionRef.document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (continuation.isActive) {
                        if (document.exists()) {
                            val question = document.toObject(QuestionDoc::class.java)
                            question?.id = document.id
                            continuation.resumeWith(Result.success(question))
                        } else {
                            continuation.resumeWith(Result.success(null))
                        }
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive) {
                        println("Unable to retrieve question data")
                        continuation.resumeWith(Result.failure(e))
                    }
                }
        }
    }

    override suspend fun createUserQuestionRef(userId: String, questionId: String) {
        val doc = mapOf(
            "user_id" to userId,
            "question_id" to questionId,
            "stars" to 0,
            "playable" to false
        )
        try {
            userQuestionRef.document(userId + questionId).set(doc).await()
            println("Ref data saved successfully!")
        } catch (e: Exception) {
            println("Error saving Ref data: ${e.message}")
        }
    }

    override suspend fun getUserQuestions(userId: String): List<QuestionDoc?> {
        return try {
            val userDocs = userQuestionRef
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            userDocs.map { doc ->
                val questionId = doc.getString("question_id")
                val stars = doc.getLong("stars")?.toInt() ?: 0
                val playable = doc.getBoolean("playable") ?: false
                val question = questionId?.let { getQuestionById(it) }
                question?.stars = stars
                question?.playable = playable
                question
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateStars(
        userId: String,
        questionId: String,
        stars: Int
    ): Flow<Response> = callbackFlow {
        val doc = userQuestionRef.document(userId + questionId)
        doc.update("stars", stars).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success("Success"))
            } else {
                val errorMessage = when (it.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password!"
                    is FirebaseAuthException -> "Auth error: ${(it.exception as FirebaseAuthException).message}"
                    else -> "Unknown error: ${it.exception?.message}"
                }
                trySend(Response.Failure(Exception(errorMessage)))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun updatePlayability(
        userId: String,
        questionId: String
    ): Flow<Response> = callbackFlow {
        val doc = userQuestionRef.document(userId + questionId)
        doc.update("playable", true).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Response.Success("Success"))
            } else {
                val errorMessage = when (it.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password!"
                    is FirebaseAuthException -> "Auth error: ${(it.exception as FirebaseAuthException).message}"
                    else -> "Unknown error: ${it.exception?.message}"
                }
                trySend(Response.Failure(Exception(errorMessage)))
            }
            close()
        }
        awaitClose()
    }

    override suspend fun deleteUserQuestions(id: String): Flow<Response> = callbackFlow {
        try {
            val userDocs = userQuestionRef
                .whereEqualTo("user_id", id)
                .get()
                .await()

            for (doc in userDocs) {
                userQuestionRef.document(doc.id).delete().await()
            }

            trySend(Response.Success("All user questions deleted successfully."))
        } catch (e: Exception) {
            trySend(Response.Failure(e))
        }
    }

}