package com.github.gasblg.models.currencies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
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
    val market: String,
    @SerialName("date")
    val date: String,
    @SerialName("rate")
    val rate: Double,
    @SerialName("percent")
    val percent: Double
)