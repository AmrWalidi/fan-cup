package com.amrwalidi.android.fancup.service

import com.amrwalidi.android.fancup.service.model.NotificationDoc
import kotlinx.coroutines.flow.Flow

interface NotificationService {
    suspend fun sendNotification(sender : String, receiver : String, type: Int , message: String) : Flow<Response>
    suspend fun receiveNotifications(userId : String) : List<NotificationDoc>?
    suspend fun readMessages(userId: String)
}