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

    @Query("select * from users LIMIT 1")
    suspend fun getUser(): DatabaseUser?

    @Query("DELETE FROM users " )
    suspend fun delete()
}