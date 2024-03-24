package com.github.gasblg.models.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemRequest(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("name_id")
    val nameId: Int,
    @SerialName("short_name_id")
    val shortNameId: Int,
    @SerialName("description_id")
    val descriptionId: Int,
    @SerialName("type")
    val type: String,
    @SerialName("market")
    val market: String
)