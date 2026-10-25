package com.winnix.base.data.remote.fake

import com.winnix.base.data.remote.sample.SampleItemDto
import com.winnix.base.data.remote.sample.SampleRemoteDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

/** Answers like the server would, after a short delay, until the real endpoint exists. */
class FakeSampleRemoteDataSource @Inject constructor() : SampleRemoteDataSource {
    override suspend fun fetchItems(): List<SampleItemDto> {
        delay(NETWORK_DELAY_MS)
        return List(ITEM_COUNT) { index ->
            SampleItemDto(
                id = "sample_$index",
                title = "Sample item ${index + 1}",
                description = "Fake data served by FakeSampleRemoteDataSource",
            )
        }
    }

    private companion object {
        const val NETWORK_DELAY_MS = 800L
        const val ITEM_COUNT = 20
    }
}
