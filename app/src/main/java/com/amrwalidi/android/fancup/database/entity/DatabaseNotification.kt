package com.amrwalidi.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notifications")
data class DatabaseNotification(
    @PrimaryKey
    val id: String,
    val sender: String,
    val active: Boolean,
    val message: String,
    val type: Int,
    val time: Date,
)