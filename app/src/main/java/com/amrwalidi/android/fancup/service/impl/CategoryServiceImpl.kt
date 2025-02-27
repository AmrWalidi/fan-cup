package com.amrwalidi.android.fancup.service.impl

import com.amrwalidi.android.fancup.service.CategoryService
import com.amrwalidi.android.fancup.service.model.CategoryDoc
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class CategoryServiceImpl @Inject constructor() : CategoryService {

    private val adminApp = FirebaseApp.getInstance("admin")
    private val categoryRef = FirebaseFirestore.getInstance(adminApp).collection("Category")
    private val storage = FirebaseStorage.getInstance(adminApp)

    override suspend fun getCategories(): List<CategoryDoc?> {
        return suspendCancellableCoroutine { continuation ->
            categoryRef.get().addOnCompleteListener { task ->
                if (continuation.isActive) {
                    val categories = mutableListOf<CategoryDoc>()
                    task.result.forEach { category ->
                        categories.add((category.toObject(CategoryDoc::class.java)))
                    }
                    continuation.resumeWith(Result.success(categories))

                }
            }.addOnFailureListener { e ->
                if (continuation.isActive) {
                    println("Unable to retrieve categories data")
                    continuation.resumeWithException(e)
                }
            }
        }
    }

    override suspend fun getCategoryBanner(name: String): ByteArray? {
        val storageCategoryBannerRef =
            storage.reference.child("category-banners/${name.trim().replace(" ", "-")}.jpg")
        val file = withContext(Dispatchers.IO) {
            File.createTempFile(name, "jpg")
        }
        return suspendCancellableCoroutine { continuation ->
            storageCategoryBannerRef.getFile(file).addOnSuccessListener {
                if (continuation.isActive) {
                    try {
                        val byteArray = file.readBytes()
                        continuation.resumeWith(Result.success(byteArray))
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    } finally {
                        file.delete()
                    }
                }
            }.addOnFailureListener { e ->
                if (continuation.isActive) {
                    println("Unable to retrieve categories data")
                    continuation.resumeWith(Result.failure(e))
                }
            }
        }

    }

    override suspend fun getCategoryImage(name: String): ByteArray? {
        val storageCategoryImageRef = storage.reference.child("category-images/${name.trim().replace(" ", "-")}.jpg")
        val file = withContext(Dispatchers.IO) {
            File.createTempFile(name, "jpg")
        }
        return suspendCancellableCoroutine { continuation ->
            storageCategoryImageRef.getFile(file).addOnSuccessListener {
                if (continuation.isActive) {
                    try {
                        val byteArray = file.readBytes()
                        continuation.resumeWith(Result.success(byteArray))
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    } finally {
                        file.delete()
                    }
                }
            }.addOnFailureListener { e ->
                if (continuation.isActive) {
                    println("Unable to retrieve categories data")
                    continuation.resumeWith(Result.failure(e))
                }
            }
        }
    }
}