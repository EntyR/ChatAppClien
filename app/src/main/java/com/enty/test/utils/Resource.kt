package com.enty.test.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Error<T>( data: T? = null,  message: String): Resource<T>(data, message)
    class Success<T>(data: T?): Resource<T>(data)
}