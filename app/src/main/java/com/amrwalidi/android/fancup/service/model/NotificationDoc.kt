package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.database.entity.DatabaseNotification
import com.google.firebase.Timestamp

data class NotificationDoc(
    var id: String = "",
    var sender: String = "",
    var receiver: String = "",
    var active: Boolean = true,
    var message: String = "",
    var type: Int = 0,
    var time: Timestamp? = null,
)

fun List<NotificationDoc>?.asDatabaseNotification(): List<DatabaseNotification>? {
    return this?.map {
        DatabaseNotification(
            id = it.id,
            sender = it.sender,
            active = it.active,
            message = it.message,
            type = it.type,
            time = it.time?.toDate()!!
        )
    }
}