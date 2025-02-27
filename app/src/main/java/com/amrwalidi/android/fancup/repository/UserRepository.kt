package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.database.entity.asDomainUser
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.service.impl.UserServiceImpl
import com.amrwalidi.android.fancup.service.model.asDatabaseUser

class UserRepository(private val database: FanCupDatabase) {

    private val userService = UserServiceImpl()

    suspend fun getUser(): User? {
        return database.userDao.getUser().asDomainUser()
    }

    suspend fun setUser(id: String) {
        val user = userService.getUserById(id).asDatabaseUser()
        user?.let { database.userDao.insert(it) }
    }

    suspend fun getUserByUsername(username: String): String? {
        return userService.getUserByUsername(username)?.id
    }

    suspend fun getUserByEmail(email: String): String? {
        return userService.getUserByEmail(email)?.id
    }

    suspend fun createUser(id : String, username: String, email: String) {
        userService.createUser(id, username, email)
    }

    suspend fun delete(){
        database.userDao.delete()
    }

}