package com.amrwalidi.android.fancup.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrwalidi.android.fancup.database.entity.DatabaseNotification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<DatabaseNotification>)

    @Query("SELECT * FROM notifications WHERE type = :type")
    suspend fun getNotifications(type: Int): List<DatabaseNotification>

    @Query("UPDATE notifications SET active = 0 WHERE type = :type")
    suspend fun readNotifications(type: Int)

    @Query("SELECT Count(*) FROM notifications WHERE active = 1 and type= 1")
    suspend fun unreadFriendRequestNotifications(): Int

    @Query("SELECT Count(*) FROM notifications WHERE active = 1 and type= 2")
    suspend fun unreadGameInvitationNotifications(): Int
}