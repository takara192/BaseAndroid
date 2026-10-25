package com.winnix.base.data.remote.json

import com.winnix.base.app.di.NetworkModule
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import org.junit.Assert.assertEquals
import org.junit.Test

class TolerantListSerializerTest {
    private val json = NetworkModule.provideJson()

    @Serializable
    private data class Item(val id: String, val count: Int = 0)

    @Serializable
    private data class Page(val items: TolerantList<Item> = emptyList())

    @Test
    fun `bad elements are skipped and the rest decode`() {
        val decoded = json.decodeFromString(
            TolerantListSerializer(Item.serializer()),
            """[{"id":"1"},{"count":2},{"id":"3","count":"oops"},{"id":"4","count":4}]""",
        )

        assertEquals(listOf(Item("1"), Item("4", 4)), decoded)
    }

    @Test
    fun `nested TolerantList field skips bad elements`() {
        val decoded = json.decodeFromString(Page.serializer(), """{"items":[{"id":"1"},{"id":null}]}""")

        assertEquals(Page(listOf(Item("1"))), decoded)
    }

    @Test
    fun `null fields fall back to their defaults`() {
        val decoded = json.decodeFromString(Item.serializer(), """{"id":"1","count":null}""")

        assertEquals(Item("1"), decoded)
    }

    @Test(expected = SerializationException::class)
    fun `a list where nothing decodes fails instead of returning empty`() {
        json.decodeFromString(TolerantListSerializer(Item.serializer()), """[{"count":1},{"count":2}]""")
    }

    @Test
    fun `an empty list stays valid`() {
        assertEquals(emptyList<String>(), json.decodeFromString(TolerantListSerializer(String.serializer()), "[]"))
    }
}
