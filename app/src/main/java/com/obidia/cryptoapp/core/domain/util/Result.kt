package com.obidia.cryptoapp.core.domain.util

sealed interface Result<out D, out E : Error> {
    data object Loading : Result<Nothing, Nothing>
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : com.obidia.cryptoapp.core.domain.util.Error>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
        is Result.Loading -> Result.Loading
    }
}

inline fun <T, E : Error> Result<T, E>.onLoading(action: () -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> this
        is Result.Loading -> {
            action()
            this
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }

        is Result.Loading -> this
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }

        is Result.Success -> this
        is Result.Loading -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>