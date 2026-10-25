package com.winnix.base.data.remote.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import timber.log.Timber

/**
 * A list field that drops elements that fail to decode instead of failing the whole response.
 * Declare nested DTO lists with this type instead of `List`; top-level list responses get the same
 * behaviour from [TolerantListConverterFactory].
 */
typealias TolerantList<T> = @Serializable(with = TolerantListSerializer::class) List<T>

class TolerantListSerializer<T>(private val elementSerializer: KSerializer<T>) : KSerializer<List<T>> {
    private val delegate = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: List<T>) = delegate.serialize(encoder, value)

    override fun deserialize(decoder: Decoder): List<T> {
        val jsonDecoder = decoder as? JsonDecoder ?: return delegate.deserialize(decoder)
        val array = jsonDecoder.decodeJsonElement() as? JsonArray
            ?: throw SerializationException("Expected a JSON array for ${descriptor.serialName}")

        val items = array.mapNotNull { element ->
            try {
                jsonDecoder.json.decodeFromJsonElement(elementSerializer, element)
            } catch (exception: IllegalArgumentException) {
                // SerializationException is an IllegalArgumentException; both mean this one element is bad.
                Timber.w(exception, "Skipping a %s that does not decode", elementSerializer.descriptor.serialName)
                null
            }
        }
        // Every element failing means the schema changed, not a few bad rows; an empty list here would
        // read as "the server has nothing" and wipe the local copy.
        if (array.isNotEmpty() && items.isEmpty()) {
            throw SerializationException("None of ${array.size} ${elementSerializer.descriptor.serialName} elements decode")
        }
        return items
    }
}
