package com.winnix.base.data.sample

import com.winnix.base.data.local.sample.SampleItemEntity
import com.winnix.base.data.remote.sample.SampleItemDto
import com.winnix.base.domain.model.SampleItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SampleItemMapperTest {
    @Test
    fun `dto keeps its server position through to the entity`() {
        val dto = SampleItemDto(id = "1", title = "Title", description = "Description")

        assertEquals(SampleItemEntity("1", "Title", "Description", position = 3), dto.toEntityOrNull(position = 3))
    }

    @Test
    fun `dto without a required field is skipped`() {
        assertNull(SampleItemDto(id = " ", title = "Title").toEntityOrNull(position = 0))
        assertNull(SampleItemDto(id = "1", title = "").toEntityOrNull(position = 0))
    }

    @Test
    fun `entity maps to the domain model`() {
        val entity = SampleItemEntity("1", "Title", "Description", position = 0)

        assertEquals(SampleItem("1", "Title", "Description"), entity.toDomain())
    }
}
