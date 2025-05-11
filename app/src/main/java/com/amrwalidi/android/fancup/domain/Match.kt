package com.amrwalidi.android.fancup.domain

data class Match(
    var players: List<Player>,
    val active: Boolean,
    val question: String,
    val createdAt: Long,
)