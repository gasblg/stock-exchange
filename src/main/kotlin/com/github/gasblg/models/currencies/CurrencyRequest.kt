package com.github.gasblg.models.currencies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRequest(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("date")
    val date: String,
    @SerialName("rate")
    val rate: Double,
    @SerialName("percent")
    val percent: Double
)