package com.agaperra.bin.domain

sealed class AppState<T>(val data: T? = null, val message: ErrorState? = null) {
    class Success<T>(data: T) : AppState<T>(data)
    data class Error<T>(val error: ErrorState) : AppState<T>()
    class Loading<T>(data: T? = null) : AppState<T>(data)
}