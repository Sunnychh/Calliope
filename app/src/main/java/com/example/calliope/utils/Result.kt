package com.example.calliope.utils

/**
 * @author Sunny
 * @date 2024/11/20/上午11:12
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

inline fun <T> safeCall(action: () -> T): Result<T> = try {
    Result.Success(action())
} catch (e: Exception) {
    Result.Error(e)
}
