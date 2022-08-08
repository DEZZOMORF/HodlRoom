package com.dezzomorf.financulator.util

sealed class UiState<out T> {

    data class Success<T>(val data: T) : UiState<T>()

    data class Error(val error: Exception) : UiState<Nothing>()

    object Loading : UiState<Nothing>()

}