package com.example.raovat_app.classes


sealed class Response<out T> {
    data class Success<out T>(val value: T) : Response<T>()
    data class SuccessWithExtra<out T, out M>(val value: T, val value1: M) : Response<T>()
    data class SuccessWith3Params<out T, out M, out S>(val value: T, val value1: M, val value2: S) : Response<T>()
}