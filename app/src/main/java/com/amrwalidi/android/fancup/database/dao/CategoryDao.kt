package com.amrwalidi.android.fancup.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrwalidi.android.fancup.database.entity.DatabaseCategory

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity : DatabaseCategory)

    @Query("SELECT * FROM categories")
    suspend fun getCategories() : List<DatabaseCategory>

    @Query("UPDATE categories SET selected = 1 WHERE id =:id")
    suspend fun selectCategory(id : Int)

    @Query("UPDATE categories SET selected = 0")
    suspend fun unselectCategories()

    @Query("SELECT * FROM categories WHERE selected = 1 LIMIT 1")
    suspend fun getSelectedCategory() : DatabaseCategory

}