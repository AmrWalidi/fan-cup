package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.domain.Notification
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.impl.NotificationServiceImpl
import com.amrwalidi.android.fancup.service.impl.UserServiceImpl
import com.amrwalidi.android.fancup.service.model.asDatabaseNotification
import com.amrwalidi.android.fancup.service.model.asDomainUser
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.Date
import java.util.concurrent.TimeUnit

class NotificationRepository(private val database: FanCupDatabase) {

    private val notificationService = NotificationServiceImpl()
    private val userService = UserServiceImpl()

    suspend fun sendNotification(
        sender: String,
        receiver: String,
        message: String,
        type: Int
    ): String {
        var msg = ""
        notificationService.sendNotification(sender, receiver, type, message).collect { res ->
            if (res is Response.Success) {
                if (type == 1)
                    msg = "Friend request have been sent"
                if (type == 2)
                    msg = "An invitation have been sent"
            }
            if (res is Response.Failure) msg = "Problem occurred while sending"
        }
        return msg
    }

    suspend fun fetchNotifications(receiver: String): List<Int> {
        val fetchedNotification = notificationService.receiveNotifications(receiver)
        val databaseNotification = fetchedNotification.asDatabaseNotification()
        databaseNotification?.let { database.notificationDao.insert(it) }
        val friendRequest = database.notificationDao.unreadFriendRequestNotifications()
        val invitations = database.notificationDao.unreadGameInvitationNotifications()
        return listOf(friendRequest, invitations)
    }

    suspend fun getNotifications(type: Int): List<Notification> = coroutineScope {
        val databaseNotifications = database.notificationDao.getNotifications(type)

        val deferredNotifications = databaseNotifications.map { dbNotification ->
            async {
                var profileImage: ByteArray? = null

                userService.getUserProfileImage(dbNotification.sender).collect { res ->
                    if (res is Response.Success && res.data is ByteArray) {
                        profileImage = res.data
                    }
                }

                if (profileImage != null) {
                    val now = Date()
                    val diffInMillis = now.time - dbNotification.time.time

                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                    val user = userService.getUserById(dbNotification.sender)
                        .asDomainUser(profileImage!!)

                    Notification(
                        id = dbNotification.id,
                        sender = user,
                        active = dbNotification.active,
                        type = dbNotification.type,
                        message = dbNotification.message,
                        timePassed = when {
                            minutes < 1 -> "just now"
                            minutes < 60 -> "$minutes minutes ago"
                            hours < 24 -> "$hours hours ago"
                            else -> "$days days ago"
                        }
                    )
                } else null
            }
        }

        deferredNotifications.awaitAll().filterNotNull()
    }

    suspend fun readNotifications(userId: String, type: Int) {
        notificationService.readNotifications(userId, type)
        database.notificationDao.readNotifications(type)
    }

}