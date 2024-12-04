package com.example.calliope.ui.viewmodel.state

/**
 * @author Sunny
 * @date 2024/11/22/下午2:40
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
