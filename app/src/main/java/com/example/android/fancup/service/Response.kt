package com.example.android.fancup.service

sealed class Response{

    data object Loading : Response()


    data class Success(
        val data: Any
    ) : Response()

    data class Failure(
        val e: Exception
    ) : Response()

}