package com.amrwalidi.android.fancup.domain

data class User(
    val id: String,
    val username: String,
    val email: String,
    val points: Int,
    val coins: Int,
    val level: String,
    val rank: String
)