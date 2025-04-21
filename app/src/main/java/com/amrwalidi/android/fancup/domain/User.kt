package com.amrwalidi.android.fancup.domain

data class User(
    val id: String,
    val username: String,
    val email: String,
    val points: Int,
    val coins: Int,
    val level: String,
    val rank: String,
    var profileImage: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (points != other.points) return false
        if (coins != other.coins) return false
        if (id != other.id) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (level != other.level) return false
        if (rank != other.rank) return false
        if (!profileImage.contentEquals(other.profileImage)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = points
        result = 31 * result + coins
        result = 31 * result + id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + level.hashCode()
        result = 31 * result + rank.hashCode()
        result = 31 * result + profileImage.contentHashCode()
        return result
    }
}