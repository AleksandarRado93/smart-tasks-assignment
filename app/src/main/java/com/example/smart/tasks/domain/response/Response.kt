package com.example.smart.tasks.domain.response

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val message: String, val data: T? = null) : Response<T>()
}