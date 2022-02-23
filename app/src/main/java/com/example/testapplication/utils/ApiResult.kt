package com.example.testapplication.utils

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception?, val statusCode: Int? = 520) :
        ApiResult<Nothing>() {
    }

    object NetworkError : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is NetworkError -> "No Network Error"
        }
    }
}