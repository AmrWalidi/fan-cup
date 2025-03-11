package com.amrwalidi.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "question_category_cross_ref",
    primaryKeys = ["questionId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = DatabaseQuestion::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DatabaseCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuestionCategoryCrossRef (
    val questionId : Long,
    val categoryId : Int
)