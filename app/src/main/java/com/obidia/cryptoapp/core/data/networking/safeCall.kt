package com.obidia.cryptoapp.core.data.networking

import com.obidia.cryptoapp.core.domain.util.NetworkError
import com.obidia.cryptoapp.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ensureActive
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