package com.github.gasblg.models.derivatives

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DerivativesRequest(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("first_trade")
    val firstTrade: String,
    @SerialName("last_trade")
    val lastTrade: String,
    @SerialName("date")
    val date: String,
    @SerialName("contract_type_id")
    val contractTypeId: Int
)