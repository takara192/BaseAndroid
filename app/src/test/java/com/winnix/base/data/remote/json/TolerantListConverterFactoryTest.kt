package com.winnix.base.data.remote.json

import com.winnix.base.app.di.NetworkModule
import kotlinx.serialization.Serializable
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Retrofit

class TolerantListConverterFactoryTest {
    private val factory = TolerantListConverterFactory(NetworkModule.provideJson())
    private val retrofit = Retrofit.Builder().baseUrl("https://example.com/").build()

    @Serializable
    data class Item(val id: String)

    /** Gives the converter the same generic return types Retrofit reads from an API interface. */
    private interface Api {
        fun list(): List<Item>
        fun single(): Item
    }

    @Test
    fun `list responses skip bad elements`() {
        val type = Api::class.java.getMethod("list").genericReturnType
        val converter = checkNotNull(factory.responseBodyConverter(type, emptyArray(), retrofit))

        val decoded = converter.convert("""[{"id":"1"},{"id":null},{}]""".toResponseBody())

        assertEquals(listOf(Item("1")), decoded)
    }

    @Test
    fun `non-list responses fall through to the next converter`() {
        val type = Api::class.java.getMethod("single").genericReturnType

        assertNull(factory.responseBodyConverter(type, emptyArray(), retrofit))
    }
}
