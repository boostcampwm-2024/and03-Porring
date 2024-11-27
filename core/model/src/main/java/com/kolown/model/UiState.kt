package com.kolown.model

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure(val error: Throwable) : UiState<Nothing>()
}

sealed class InitUiState<out T> {
    data object Init : InitUiState<Nothing>()
    data object Loading : InitUiState<Nothing>()
    data class Success<T>(val data: T) : InitUiState<T>()
    data class Failure(val error: Throwable) : InitUiState<Nothing>()
}
