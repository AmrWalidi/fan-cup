package com.example.android.fancup.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android.fancup.database.entity.DatabaseUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DatabaseUser)

    @Update
    suspend fun update(entity: DatabaseUser)

    @Query("select * from databaseuser")
    suspend fun get(): DatabaseUser?

    @Query("DELETE FROM databaseuser WHERE id = :id" )
    suspend fun delete(id: Int)
}