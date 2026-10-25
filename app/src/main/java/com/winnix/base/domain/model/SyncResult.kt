package com.winnix.base.domain.model

/** Outcome of refreshing local data from the server; the UI keeps showing what Room already holds either way. */
sealed interface SyncResult {
    data object Success : SyncResult

    data class Failure(val error: DataError) : SyncResult
}

enum class DataError {
    /** No connection, timeout or DNS failure; worth retrying. */
    Network,

    /** HTTP 5xx. */
    Server,

    /** HTTP 4xx. */
    Client,

    /** The response did not match the expected DTO. */
    Parse,
    Unknown,
}
