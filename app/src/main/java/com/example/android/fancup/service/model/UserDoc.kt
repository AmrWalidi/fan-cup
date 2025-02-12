package com.example.android.fancup.service.model

import com.example.android.fancup.database.entity.DatabaseUser
import com.example.android.fancup.domain.User

data class UserDoc(
    val id: Int,
    val username: String,
    val email: String,
    val points: Int,
    val coins: Int,
    val level: String
)

fun UserDoc?.asDatabaseUser(): DatabaseUser? {
    return this?.let {
        DatabaseUser(
            id = id,
            username = username,
            email = email,
            points = points,
            coins = coins,
            level = level
        )
    }
}

fun UserDoc?.asDomainUser(): User? {
    return this?.let {
        User(
            id = id,
            username = username,
            email = email,
            points = points,
            coins = coins,
            level = level
        )
    }
}

