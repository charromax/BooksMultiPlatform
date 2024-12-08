package com.plcoding.bookpedia.core.data

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {
    return when (response.status.value) {
        in (200..299) -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                return Result.Error(DataError.Remote.SerializationError)
            }
        }

        408 -> Result.Error(DataError.Remote.RequestTimeout)
        429 -> Result.Error(DataError.Remote.TooManyRequests)
        in (500..599) -> Result.Error(DataError.Remote.ServerError)
        else -> Result.Error(DataError.Remote.UnknownError)
    }
}

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return Result.Error(DataError.Remote.RequestTimeout)
    } catch (e: UnresolvedAddressException) {
        return Result.Error(DataError.Remote.NoInternetConnection)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UnknownError)
    }
    return responseToResult(response)
}
