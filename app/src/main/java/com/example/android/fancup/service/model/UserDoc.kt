package com.example.android.fancup.service.model

import com.example.android.fancup.database.entity.DatabaseUser

data class UserDoc(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val points: Int = 0,
    val coins: Int = 0,
    val level: Int = 0,
    val rank: Int = 0
)

fun UserDoc?.asDatabaseUser(): DatabaseUser? {
    return this?.let {
        DatabaseUser(
            id = id,
            username = username,
            email = email,
            points = points,
            coins = coins,
            level = level,
            rank = rank
        )
    }
}

