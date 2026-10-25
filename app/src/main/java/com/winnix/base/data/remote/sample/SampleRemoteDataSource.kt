package com.winnix.base.data.remote.sample

import javax.inject.Inject

/** Where sample items come from; debug builds can swap in a fake, see `src/debug`. */
interface SampleRemoteDataSource {
    suspend fun fetchItems(): List<SampleItemDto>
}

class RetrofitSampleRemoteDataSource @Inject constructor(
    private val api: SampleApi,
) : SampleRemoteDataSource {
    override suspend fun fetchItems(): List<SampleItemDto> = api.getItems()
}
