package com.amrwalidi.android.fancup.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amrwalidi.android.fancup.database.entity.DatabaseQuestion
import com.amrwalidi.android.fancup.database.entity.QuestionCategoryCrossRef

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: DatabaseQuestion) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionCategoryCrossRef(crossRef: QuestionCategoryCrossRef)

    @Query("SELECT * FROM questions WHERE id=:id")
    suspend fun getQuestionById(id: Long) : DatabaseQuestion

    @Transaction
    @Query(
        """
    SELECT * FROM questions 
    WHERE id IN (
        SELECT questionId FROM question_category_cross_ref
        WHERE categoryId = :categoryId
    ) ORDER BY difficulty ASC
    """
    )
    suspend fun getQuestionsByCategory(categoryId: Int): List<DatabaseQuestion>

}