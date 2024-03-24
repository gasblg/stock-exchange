package com.github.gasblg.models.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemResponse(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("name")
    val name: String,
    @SerialName("short_name")
    val shortName: String,
    @SerialName("description")
    val description: String,
    @SerialName("type")
    val type: String,
    @SerialName("market")
    val market: String
)