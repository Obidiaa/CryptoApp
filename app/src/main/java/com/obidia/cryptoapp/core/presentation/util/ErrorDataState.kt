package com.obidia.cryptoapp.core.presentation.util

import com.obidia.cryptoapp.R
import com.obidia.cryptoapp.core.domain.util.NetworkError

data class ErrorDataState(
    val imageDark: Int,
    val imageLight: Int,
    val message: Int
)

fun NetworkError.toErrorDataState(): ErrorDataState {
    return when (this) {
        NetworkError.REQUEST_TIMEOUT -> ErrorDataState(
            R.drawable.ic_408_dark,
            R.drawable.ic_408_light,
            R.string.error_request_timeout
        )

        NetworkError.TOO_MANY_REQUEST -> ErrorDataState(
            R.drawable.ic_429_dark,
            R.drawable.ic_429_light,
            R.string.error_too_many_requests
        )

        NetworkError.NO_INTERNET -> ErrorDataState(
            R.drawable.ic_no_internet_dark,
            R.drawable.ic_no_internet_light,
            R.string.error_no_internet
        )

        NetworkError.SERVER_ERROR -> ErrorDataState(
            R.drawable.ic_server_error_dark,
            R.drawable.ic_server_error_light,
            R.string.error_unknown
        )

        NetworkError.SERIALIZATION -> ErrorDataState(
            R.drawable.ic_serialization_dark,
            R.drawable.ic_serialization_light,
            R.string.error_serialization
        )

        NetworkError.UNKNOWN -> ErrorDataState(
            R.drawable.ic_unknown_dark,
            R.drawable.ic_unknown_light,
            R.string.error_unknown
        )
    }
}
