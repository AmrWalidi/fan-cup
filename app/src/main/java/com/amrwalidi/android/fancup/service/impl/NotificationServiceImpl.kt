package com.amrwalidi.android.fancup.service.impl

import com.amrwalidi.android.fancup.service.model.NotificationDoc
import com.amrwalidi.android.fancup.service.NotificationService
import com.amrwalidi.android.fancup.service.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotificationServiceImpl : NotificationService {

    private val notificationRef = FirebaseFirestore.getInstance().collection("notifications")

    override suspend fun sendNotification(
        sender: String,
        receiver: String,
        type: Int,
        message: String
    ): Flow<Response> = callbackFlow {
        val notification = NotificationDoc()

        notification.sender = sender
        notification.receiver = receiver
        notification.message = message
        notification.time = Timestamp.now()
        notification.type = type
        val id = sender + receiver + (notification.time?.toDate()?.time ?: "")
        notification.id = id

        notificationRef.document(id)
            .set(notification)
            .addOnSuccessListener {
                trySend(Response.Success("Notification send successfully!"))
                close()
            }
            .addOnFailureListener { e ->
                trySend(Response.Failure(Exception(e.message)))
                close()
            }
        awaitClose()
    }

    override suspend fun receiveNotifications(userId: String): List<NotificationDoc>? {
        return try {
            val notificationDocs = notificationRef
                .whereEqualTo("receiver", userId)
                .get()
                .await()

            notificationDocs.map { doc ->
                doc.toObject(NotificationDoc::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun readNotifications(userId: String, type: Int) {
        try {
            val notificationDocs = notificationRef
                .whereEqualTo("receiver", userId)
                .whereEqualTo("type", type)
                .get()
                .await()

            for (doc in notificationDocs) {
                notificationRef.document(doc.id)
                    .update("active", false)
                    .await()
            }
        } catch (e: Exception) {
            print("Can't update notifications")
        }
    }
}