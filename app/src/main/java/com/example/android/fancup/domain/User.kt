package com.example.android.fancup.domain

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val points: Int,
    val coins: Int,
    val level: String
)
