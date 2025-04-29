package com.amrwalidi.android.fancup.domain


data class Notification (
    val id : Int,
    val sender : User?,
    val active : Boolean,
    val type : Int,
    val message: String,
    val timePassed : String,
)
