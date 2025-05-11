package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.domain.Player

data class PlayerDoc(
    val id: String = "",
    val ready: Boolean = false
)

fun PlayerDoc.toDomain(): Player {
    return Player(
        id = this.id,
        ready = this.ready
    )
}