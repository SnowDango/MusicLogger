package com.snowdango.musiclogger.model

sealed class ModelState<out R> {
    data class Success<out T>(val data: T) : ModelState<T>()
    object Loading : ModelState<Nothing>()
    data class Failed(val error: Throwable) : ModelState<Nothing>()
}