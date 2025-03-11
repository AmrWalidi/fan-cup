package com.amrwalidi.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amrwalidi.android.fancup.domain.Question

@Entity(tableName = "questions")
data class DatabaseQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0 ,
    val text: String,
    val answers: List<String>,
    val options: List<String>,
    val type: Int,
    val difficulty: Int
)


fun List<DatabaseQuestion>?.asDomainQuestion(): List<Question>? {
    return this?.map { databaseQuestion ->
        Question(
            id = databaseQuestion.id,
            text = databaseQuestion.text,
            answers = databaseQuestion.answers,
            options = databaseQuestion.options,
            type = databaseQuestion.type,
            difficulty = databaseQuestion.difficulty
        )
    }
}
