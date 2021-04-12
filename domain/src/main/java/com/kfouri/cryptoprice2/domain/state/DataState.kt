package com.kfouri.cryptoprice2.domain.state

sealed class DataState<out T> {

    data class InProgress<T>(val progressData: T? = null): DataState<T>()
    data class Success<T>(val data: T? = null): DataState<T>()
    data class Failure<T>(val error: Throwable? = null): DataState<T>()

    fun peekDataOrNull(): T? =
        when (this) {
            is Success -> data
            is InProgress -> progressData
            else -> null
        }

    fun peekFailureOrNull(): Throwable? =
        when (this) {
            is Failure -> error
            else -> null
        }
}