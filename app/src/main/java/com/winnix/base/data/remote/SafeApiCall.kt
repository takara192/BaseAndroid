package com.winnix.base.data.remote

import com.winnix.base.domain.model.DataError
import com.winnix.base.domain.model.SyncResult
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection

/** Runs one sync step and maps any failure to a [DataError]; cancellation is never swallowed. */
suspend fun safeApiCall(block: suspend () -> Unit): SyncResult = try {
    block()
    SyncResult.Success
} catch (exception: CancellationException) {
    throw exception
} catch (exception: Exception) {
    Timber.w(exception, "Sync failed")
    SyncResult.Failure(exception.toDataError())
}

private fun Exception.toDataError(): DataError = when (this) {
    is IOException -> DataError.Network
    is HttpException -> if (code() >= HttpURLConnection.HTTP_INTERNAL_ERROR) DataError.Server else DataError.Client
    is SerializationException -> DataError.Parse
    else -> DataError.Unknown
}
