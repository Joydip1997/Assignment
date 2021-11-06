package com.example.assignment.Utils


sealed class Resource<out T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Error<out T>(data: T?, e: Exception?) : Resource<T>(data, e)
}

data class ApiError(override val message: String, val statusCode: Int) : Exception(message)