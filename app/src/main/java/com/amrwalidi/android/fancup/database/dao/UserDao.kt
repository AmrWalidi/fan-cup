package com.amrwalidi.android.fancup.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amrwalidi.android.fancup.database.entity.DatabaseUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DatabaseUser)

    @Update
    suspend fun update(entity: DatabaseUser)

    @Query("select * from users LIMIT 1")
    suspend fun getUser(): DatabaseUser?

    @Query("UPDATE users SET points = :points WHERE id = :id")
    suspend fun updatePoints(id: String, points: Int)

    @Query("UPDATE users SET coins = :coins WHERE id = :id")
    suspend fun updateCoins(id: String, coins: Int)

    @Query("UPDATE users SET level = level + 1 WHERE id = :id")
    suspend fun updateLevel(id: String)

    @Query("UPDATE users SET profileImage = :image WHERE id = :id")
    suspend fun updateProfile(id: String, image: ByteArray)

    @Query("UPDATE users SET friends = :friends WHERE id = :id")
    suspend fun addFriend(id: String, friends: String)

    @Query("SELECT friends FROM USERS WHERE id = :id")
    suspend fun getFriends(id: String) : String

    @Query("UPDATE users SET inLobby = 1 WHERE id = :id")
    suspend fun enterLobby(id: String)

    @Query("UPDATE users SET inLobby = 0 WHERE id = :id")
    suspend fun exitLobby(id: String)

    @Query("DELETE FROM users ")
    suspend fun delete()
}