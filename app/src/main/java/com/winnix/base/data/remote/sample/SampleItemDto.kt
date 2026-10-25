package com.winnix.base.data.remote.sample

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SampleItemDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String = "",
)
