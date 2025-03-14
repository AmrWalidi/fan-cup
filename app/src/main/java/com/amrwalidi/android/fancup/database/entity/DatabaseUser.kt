package com.amrwalidi.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amrwalidi.android.fancup.domain.User

@Entity(tableName = "users")
data class DatabaseUser(
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val points: Int,
    val coins: Int,
    val level: Int,
    val rank: Int
)

fun DatabaseUser?.asDomainUser(): User? {
    return this?.run {
        User(
            id = id,
            username = username,
            email = email,
            points = points,
            coins = coins,
            level = level.toString(),
            rank = rank.toString()
        )
    }
}