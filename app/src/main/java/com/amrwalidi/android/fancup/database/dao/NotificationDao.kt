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

    @Query("SELECT * FROM notifications")
    suspend fun getNotifications(): List<DatabaseNotification>
}