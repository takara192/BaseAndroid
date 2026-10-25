package com.winnix.base.data.local.sample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = SampleItemEntity.TABLE, indices = [Index("position")])
data class SampleItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    /** Server order, so the list reads back the way the API returned it. */
    @ColumnInfo(name = "position") val position: Int,
) {
    companion object {
        const val TABLE = "sample_items"
    }
}
