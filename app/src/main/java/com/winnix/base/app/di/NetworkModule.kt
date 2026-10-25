package com.winnix.base.app.di

import com.winnix.base.BuildConfig
import com.winnix.base.data.remote.json.TolerantListConverterFactory
import com.winnix.base.data.remote.sample.SampleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = false
        // A null or unknown enum value falls back to the DTO field's default instead of failing.
        coerceInputValues = true
        isLenient = true
    }

    /** One client for the API and every image download, so they share a connection pool. */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.ENABLE_LOG) {
            val logging = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
            addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
        }
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        // Must come first: it claims List responses and leaves everything else to kotlinx.
        .addConverterFactory(TolerantListConverterFactory(json))
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideSampleApi(retrofit: Retrofit): SampleApi = retrofit.create()
}
