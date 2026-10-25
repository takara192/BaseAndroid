package com.winnix.base.data.remote.sample

import retrofit2.http.GET

interface SampleApi {
    @GET("items")
    suspend fun getItems(): List<SampleItemDto>
}
