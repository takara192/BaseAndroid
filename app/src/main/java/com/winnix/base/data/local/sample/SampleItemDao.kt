package com.winnix.base.data.local.sample

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SampleItemDao {
    @Query("SELECT * FROM sample_items ORDER BY position")
    fun observeAll(): Flow<List<SampleItemEntity>>

    @Upsert
    suspend fun upsert(items: List<SampleItemEntity>)

    @Query("DELETE FROM sample_items WHERE id NOT IN (:ids)")
    suspend fun deleteAllExcept(ids: List<String>)

    /** Makes the table match [items] in one transaction, so observers re-query once. */
    @Transaction
    suspend fun replaceAll(items: List<SampleItemEntity>) {
        deleteAllExcept(items.map { it.id })
        upsert(items)
    }
}
