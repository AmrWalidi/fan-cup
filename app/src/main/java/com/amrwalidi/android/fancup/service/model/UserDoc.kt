package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.database.entity.DatabaseUser

data class UserDoc(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    val points: Int = 0,
    val coins: Int = 0,
    val level: Int = 1,
    val rank: Int = 1
)

fun UserDoc?.asDatabaseUser(): DatabaseUser? {
    return this?.run {
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

