package com.amrwalidi.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amrwalidi.android.fancup.domain.Question

@Entity(tableName = "questions")
data class DatabaseQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val answers: List<String>,
    val options: List<String>,
    val type: Int,
    val difficulty: Int
)


fun List<DatabaseQuestion>?.asDomainQuestion(): List<Question>? {
    return this?.mapIndexed { idx, databaseQuestion ->
        Question(
            id = databaseQuestion.id,
            listId = idx.plus(1).toString(),
            text = databaseQuestion.text,
            answers = databaseQuestion.answers,
            options = databaseQuestion.options,
            type = databaseQuestion.type,
            difficulty = databaseQuestion.difficulty
        )
    }
}

fun DatabaseQuestion?.asDomainQuestion(): Question? {
    return this?.run {
        Question(
            id = id,
            text = text,
            answers = answers,
            options = options,
            type = type,
            difficulty = difficulty
        )
    }

}