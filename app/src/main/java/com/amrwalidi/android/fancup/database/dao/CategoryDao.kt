package com.amrwalidi.android.fancup.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrwalidi.android.fancup.database.entity.DatabaseCategory

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getCategories() : List<DatabaseCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity : DatabaseCategory)
}