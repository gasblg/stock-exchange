package com.github.gasblg.models.shares

import com.github.gasblg.models.candles.CandleResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharesDetailResponse(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("date")
    val date: String,
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
    val percent: Double,
    @SerialName("candles")
    val candles: List<CandleResponse>? = null
)