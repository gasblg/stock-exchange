package com.github.gasblg.models.candles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CandleRequest(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("date")
    val date: String,
    @SerialName("open")
    val open: Double,
    @SerialName("low")
    val low: Double,
    @SerialName("high")
    val high: Double,
    @SerialName("close")
    val close: Double,
    @SerialName("volume")
    val volume: Int,
    @SerialName("price")
    val price: Double,
    @SerialName("percent")
    val percent: Double
)


