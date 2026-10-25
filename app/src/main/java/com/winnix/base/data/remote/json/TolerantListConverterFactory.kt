package com.winnix.base.data.remote.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Decodes every `List<T>` response body with [TolerantListSerializer], so one bad element never
 * fails the call. Other types return null and fall through to the next converter.
 */
class TolerantListConverterFactory(private val json: Json) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        if (type !is ParameterizedType || getRawType(type) != List::class.java) return null

        @Suppress("UNCHECKED_CAST")
        val elementSerializer = json.serializersModule.serializer(getParameterUpperBound(0, type))
            as KSerializer<Any?>
        val serializer = TolerantListSerializer(elementSerializer)
        return Converter<ResponseBody, List<Any?>> { body -> body.use { json.decodeFromString(serializer, it.string()) } }
    }
}
