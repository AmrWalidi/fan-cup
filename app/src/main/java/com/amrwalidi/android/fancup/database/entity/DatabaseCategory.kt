package com.amrwalidi.android.fancup.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amrwalidi.android.fancup.domain.Category

@Entity(tableName = "categories")
data class DatabaseCategory(
    @PrimaryKey
    val id: Int,
    val name: String,
    val bannerImage: ByteArray,
    val image: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatabaseCategory

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

fun List<DatabaseCategory>?.asDomainCategories(): List<Category>? {
    val categories = this?.map {
        Category(
            id = it.id,
            name = it.name,
            bannerImage = it.bannerImage,
            image = it.image
        )
    }
    return categories
}
