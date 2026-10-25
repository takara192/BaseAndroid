package com.winnix.base.app.di

import android.content.Context
import androidx.room.Room
import com.winnix.base.data.local.AppDatabase
import com.winnix.base.data.local.sample.SampleItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()

    @Provides
    fun provideSampleItemDao(database: AppDatabase): SampleItemDao = database.sampleItemDao()
}
