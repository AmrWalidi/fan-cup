package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.database.entity.asDomainCategories
import com.amrwalidi.android.fancup.domain.Category
import com.amrwalidi.android.fancup.service.impl.CategoryServiceImpl
import com.amrwalidi.android.fancup.service.model.asDatabaseCategory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class CategoryRepository(private val database: FanCupDatabase) {

    private val categoryService = CategoryServiceImpl()

    suspend fun getCategories(): List<Category>? {
        return database.categoryDao.getCategories().asDomainCategories()
    }

    suspend fun fetchCategories() = coroutineScope {

        val categories = categoryService.getCategories()

        categories.map { category ->
            async {
                val banner = categoryService.getCategoryBanner(category!!.name)
                val image = categoryService.getCategoryImage(category.name)
                val c = category.asDatabaseCategory(banner!!, image!!)
                database.categoryDao.insert(c!!)
            }
        }.awaitAll()
    }
}