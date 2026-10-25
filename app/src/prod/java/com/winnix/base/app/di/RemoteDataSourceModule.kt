package com.winnix.base.app.di

import com.winnix.base.data.remote.sample.RetrofitSampleRemoteDataSource
import com.winnix.base.data.remote.sample.SampleRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Staging and release bindings: always the real sources. Debug has its own copy in `src/debug`. */
@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteDataSourceModule {
    @Binds
    abstract fun bindSampleRemoteDataSource(source: RetrofitSampleRemoteDataSource): SampleRemoteDataSource
}
