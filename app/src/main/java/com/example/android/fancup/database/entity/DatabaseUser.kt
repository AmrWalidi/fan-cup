package com.example.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseUser(
    @PrimaryKey
    val id: Int,
)