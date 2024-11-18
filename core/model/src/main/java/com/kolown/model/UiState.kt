package com.kolown.model

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure(val error: Throwable) : UiState<Nothing>()
}
