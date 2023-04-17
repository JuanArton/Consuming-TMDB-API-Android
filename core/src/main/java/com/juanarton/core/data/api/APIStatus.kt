package com.example.core.data.api

sealed class ApiStatus<out T> {
    data class Success<out T>(val data: T) : ApiStatus<T>()
    data class Failed(val error: String) : ApiStatus<Nothing>()
    object Empty : ApiStatus<Nothing>()
}