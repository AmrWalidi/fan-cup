package com.amrwalidi.android.fancup.domain

data class Category(
    val id: Int,
    val name: String,
    val bannerImage: ByteArray,
    val image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (name != other.name) return false
        if (!bannerImage.contentEquals(other.bannerImage)) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + bannerImage.contentHashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}