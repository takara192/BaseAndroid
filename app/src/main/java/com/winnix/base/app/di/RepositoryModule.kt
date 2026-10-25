package com.winnix.base.app.di

import com.winnix.base.data.sample.SampleRepositoryImpl
import com.winnix.base.domain.repository.SampleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindSampleRepository(repository: SampleRepositoryImpl): SampleRepository
}
