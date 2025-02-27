package com.amrwalidi.android.fancup.service

import com.amrwalidi.android.fancup.service.model.CategoryDoc

interface CategoryService {
    suspend fun getCategories(): List<CategoryDoc?>
    suspend fun getCategoryBanner(name: String): ByteArray?
    suspend fun getCategoryImage(name: String): ByteArray?
}