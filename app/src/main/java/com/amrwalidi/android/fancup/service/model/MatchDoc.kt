package com.amrwalidi.android.fancup.service.model

import com.amrwalidi.android.fancup.domain.Match

data class MatchDoc(
    var players: List<PlayerDoc> = listOf(),
    val active: Boolean = false,
    val question: String = "",
    val created_at: com.google.firebase.Timestamp? = null,
    val winner: String = ""
)

fun MatchDoc?.asMatchDomain(): Match? {
    return this?.run {
        Match(
            players = players.map { it.toDomain() },
            active = active,
            question = question,
            createdAt = created_at?.seconds ?: 0L
        )
    }
}