package com.winnix.base.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.winnix.base.data.local.sample.SampleItemDao
import com.winnix.base.data.local.sample.SampleItemEntity

@Database(entities = [SampleItemEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleItemDao(): SampleItemDao

    companion object {
        const val NAME = "app.db"
    }
}
