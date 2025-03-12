package com.amrwalidi.android.fancup.domain

data class Question(
    val id : Long,
    val listId : String,
    val text: String,
    val answers: List<String>,
    val options: List<String>,
    val type: Int,
    val difficulty: Int
)
