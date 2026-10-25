package com.winnix.base.data.sample

import com.winnix.base.app.di.IoDispatcher
import com.winnix.base.data.local.sample.SampleItemDao
import com.winnix.base.data.remote.safeApiCall
import com.winnix.base.data.remote.sample.SampleRemoteDataSource
import com.winnix.base.domain.model.SampleItem
import com.winnix.base.domain.model.SyncResult
import com.winnix.base.domain.repository.SampleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/** Room is the source of truth; [refresh] only writes the server's answer into it. */
@Singleton
class SampleRepositoryImpl @Inject constructor(
    private val dao: SampleItemDao,
    private val remote: SampleRemoteDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SampleRepository {
    override fun observeItems(): Flow<List<SampleItem>> =
        dao.observeAll().distinctUntilChanged().map { rows -> rows.map { it.toDomain() } }

    override suspend fun refresh(): SyncResult = withContext(ioDispatcher) {
        safeApiCall {
            val items = remote.fetchItems()
            val entities = items.mapIndexedNotNull { index, dto -> dto.toEntityOrNull(position = index) }
            if (entities.size < items.size) Timber.w("Skipped %d invalid sample items", items.size - entities.size)
            // Nothing valid in a non-empty answer is a broken response; keep the local copy.
            if (items.isNotEmpty() && entities.isEmpty()) throw SerializationException("No valid sample items")
            dao.replaceAll(entities)
        }
    }
}
