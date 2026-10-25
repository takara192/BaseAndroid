package com.winnix.base.app.di

import com.winnix.base.BuildConfig
import com.winnix.base.data.remote.fake.FakeSampleRemoteDataSource
import com.winnix.base.data.remote.sample.RetrofitSampleRemoteDataSource
import com.winnix.base.data.remote.sample.SampleRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider

/**
 * Debug bindings: [BuildConfig.USE_FAKE_DATA] picks the fake or the real source. Staging and release
 * use `src/prod`, which binds only the real sources, so no fake code ships in them.
 */
@InstallIn(SingletonComponent::class)
@Module
object RemoteDataSourceModule {
    @Provides
    fun provideSampleRemoteDataSource(
        fake: Provider<FakeSampleRemoteDataSource>,
        real: Provider<RetrofitSampleRemoteDataSource>,
    ): SampleRemoteDataSource = if (BuildConfig.USE_FAKE_DATA) fake.get() else real.get()
}
