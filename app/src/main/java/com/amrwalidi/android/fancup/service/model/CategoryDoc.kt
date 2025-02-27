package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.database.entity.DatabaseCategory

data class CategoryDoc(
    val id: Int = 0,
    val name: String = "",
)

fun CategoryDoc?.asDatabaseCategory(
    bannerImage: ByteArray,
    image: ByteArray
): DatabaseCategory? {
    return this?.let {
        DatabaseCategory(
            id = id, name = name, bannerImage = bannerImage, image = image
        )
    }
}