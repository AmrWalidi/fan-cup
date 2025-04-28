package com.amrwalidi.android.fancup.repository

import android.content.Context
import android.net.Uri
import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.database.entity.asDomainUser
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.impl.UserServiceImpl
import com.amrwalidi.android.fancup.service.model.asDatabaseUser
import com.amrwalidi.android.fancup.service.model.asDomainUser

class UserRepository(private val database: FanCupDatabase) {

    private val userService = UserServiceImpl()

    suspend fun getUser(): User? {
        return database.userDao.getUser().asDomainUser()
    }

    suspend fun getUserByUsername(username: String): String? {
        return userService.getUserByUsername(username)?.id
    }

    suspend fun getUserByEmail(email: String): String? {
        return userService.getUserByEmail(email)?.id
    }

    suspend fun setUser(id: String) {
        userService.getUserProfileImage(id).collect { res ->
            val profileImage =
                if (res is Response.Success && res.data is ByteArray) res.data else null
            val user = userService.getUserById(id).asDatabaseUser(profileImage)
            user?.let { database.userDao.insert(it) }
        }
    }

    suspend fun createUser(id: String, username: String, email: String) {
        userService.createUser(id, username, email)

        userService.getUserProfileImage(id).collect { res ->
            val profileImage =
                if (res is Response.Success && res.data is ByteArray) res.data else null
            val user = userService.getUserById(id).asDatabaseUser(profileImage)
            user?.let { database.userDao.insert(it) }
        }
    }

    suspend fun getUsers(username : String = ""): List<User>? {
        val userId = getUser()?.id
        val users = userId?.let { userService.getUsers(it, username) } ?: return null
        val result = mutableListOf<User>()

        for (user in users) {
            var profileImage: ByteArray? = null
            userService.getUserProfileImage(user.id).collect { res ->
                if (res is Response.Success && res.data is ByteArray) {
                    profileImage = res.data
                }
            }
            user.asDomainUser(profileImage)?.let { result.add(it) }
        }

        return result
    }


    suspend fun delete() {
        database.userDao.delete()
    }

    suspend fun updatePoints(id: String, points: Int) {
        userService.updatePoints(id, points).collect { res ->
            if (res is Response.Success)
                database.userDao.updatePoints(id, points)
        }
    }

    suspend fun updateCoins(id: String, coins: Int) {
        userService.updateCoins(id, coins).collect { res ->
            if (res is Response.Success)
                database.userDao.updateCoins(id, coins)
        }
    }

    suspend fun updateLevel(id: String, level: Int) {
        userService.updateLevel(id, level).collect { res ->
            if (res is Response.Success)
                database.userDao.updateLevel(id)
        }
    }

    suspend fun uploadImage(context: Context, id: String, image: Uri) {
        userService.uploadUserProfileImage(id, image).collect { res ->
            if (res is Response.Success) {
                val imageArray = try {
                    context.contentResolver.openInputStream(image)?.use { inputStream ->
                        inputStream.readBytes()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                if (imageArray != null) {
                    database.userDao.updateProfile(id, imageArray)
                }
            }
        }
    }

}