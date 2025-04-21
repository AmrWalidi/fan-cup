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
    val rank: Int,
    val profileImage: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatabaseUser

        if (points != other.points) return false
        if (coins != other.coins) return false
        if (level != other.level) return false
        if (rank != other.rank) return false
        if (id != other.id) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (!profileImage.contentEquals(other.profileImage)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = points
        result = 31 * result + coins
        result = 31 * result + level
        result = 31 * result + rank
        result = 31 * result + id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + profileImage.contentHashCode()
        return result
    }
}

fun DatabaseUser?.asDomainUser(): User? {
    return this?.run {
        User(
            id = id,
            username = username,
            email = email,
            points = points,
            coins = coins,
            level = level.toString(),
            rank = rank.toString(),
            profileImage = profileImage
        )
    }
}