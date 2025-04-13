package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.database.entity.DatabaseQuestion

var index: Long = 1

data class QuestionDoc(
    var id: String = "",
    val text: String = "",
    val correct_answer: List<String> = listOf(),
    val options: List<String> = listOf(),
    val question_type: Int = 0,
    val categories: List<Int> = listOf(),
    val difficulty_level: String = "",
)

fun QuestionDoc?.asDatabaseQuestion(): DatabaseQuestion? {
    return this?.run {
        DatabaseQuestion(
            id = id,
            rowID = index++,
            text = text,
            answers = correct_answer,
            options = options,
            difficulty = when (difficulty_level) {
                "easy" -> 1
                "medium" -> 2
                "hard" -> 3
                "very hard" -> 4
                else -> 2
            },
            type = question_type,
            stars = 0,
            playable = false
        )
    }

}