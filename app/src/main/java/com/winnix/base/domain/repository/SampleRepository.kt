package com.winnix.base.domain.repository

import com.winnix.base.domain.model.SampleItem
import com.winnix.base.domain.model.SyncResult
import kotlinx.coroutines.flow.Flow

interface SampleRepository {
    /** Items stored locally, in display order; emits again whenever a refresh changes them. */
    fun observeItems(): Flow<List<SampleItem>>

    /** Pulls the latest items from the server into local storage. */
    suspend fun refresh(): SyncResult
}
