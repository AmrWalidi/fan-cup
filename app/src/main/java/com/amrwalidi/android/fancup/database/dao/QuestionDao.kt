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
    suspend fun insert(question: DatabaseQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionCategoryCrossRef(crossRef: QuestionCategoryCrossRef)

    @Query("SELECT * FROM questions WHERE id=:id")
    suspend fun getQuestionById(id: String): DatabaseQuestion

    @Transaction
    @Query(
        """
    SELECT * FROM questions 
    WHERE id IN (
        SELECT questionId FROM question_category_cross_ref
        WHERE categoryId = :categoryId
    ) ORDER BY difficulty ASC, rowID ASC
    """
    )
    suspend fun getQuestionsByCategory(categoryId: Int): List<DatabaseQuestion>

    @Query("UPDATE questions SET stars = :stars WHERE id=:id")
    suspend fun updateStars(id: String, stars: Int)

    @Query("UPDATE questions SET playable = 1 WHERE id=:id")
    suspend fun updatePlayability(id: String)


}