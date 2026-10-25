package com.winnix.base.data.sample

import com.winnix.base.data.local.sample.SampleItemEntity
import com.winnix.base.data.remote.sample.SampleItemDto
import com.winnix.base.domain.model.SampleItem

/** Null when a required field is blank; the caller skips that row. */
fun SampleItemDto.toEntityOrNull(position: Int): SampleItemEntity? {
    if (id.isBlank() || title.isBlank()) return null
    return SampleItemEntity(
        id = id,
        title = title,
        description = description,
        position = position,
    )
}

fun SampleItemEntity.toDomain() = SampleItem(
    id = id,
    title = title,
    description = description,
)
