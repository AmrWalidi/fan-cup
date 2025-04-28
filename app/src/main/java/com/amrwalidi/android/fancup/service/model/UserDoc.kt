package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.database.entity.DatabaseUser
import com.amrwalidi.android.fancup.domain.User

data class UserDoc(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    val points: Int = 0,
    val coins: Int = 0,
    val level: Int = 1,
    val rank: Int = 1
)

fun UserDoc?.asDatabaseUser(profileImage: ByteArray?): DatabaseUser? {
    return this?.run {
        DatabaseUser(
            id = id,
            username = username,
            email = email,
            points = points,
            coins = coins,
            level = level,
            rank = rank,
            profileImage = profileImage
        )
    }
}

fun UserDoc?.asDomainUser(profileImage: ByteArray?): User? {
    return this?.run {
        User(
            id = id,
            username = username,
            email =  email,
            points = points,
            coins = coins,
            level = level.toString(),
            rank = rank.toString(),
            profileImage = profileImage
        )
    }
}

