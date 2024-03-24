package com.github.gasblg.models.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsRequest(
    @SerialName("title_id")
    val titleId: Int,
    @SerialName("date")
    val date: String,
    @SerialName("body_id")
    val bodyId: Int
)