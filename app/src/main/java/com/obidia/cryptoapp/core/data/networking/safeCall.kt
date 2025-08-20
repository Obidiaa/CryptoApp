package com.obidia.cryptoapp.core.data.networking

import android.util.Log
import com.obidia.cryptoapp.core.domain.util.NetworkError
import com.obidia.cryptoapp.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    } catch (e: IOException) {
        return Result.Error(NetworkError.NO_INTERNET)
    }

    return responseResult(response)
}

suspend inline fun <reified T> safeCallTest(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    } catch (e: IOException) {
        return Result.Error(NetworkError.NO_INTERNET)
    }

    return responseResult(response)
}

suspend inline fun <reified T> responseToResultTest(
    response: HttpResponse
): Result<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Log.d("kesini suc", response.body<T>().toString())
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }

        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUEST)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}