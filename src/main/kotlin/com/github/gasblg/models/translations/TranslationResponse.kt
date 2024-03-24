package com.github.gasblg.models.translations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("content_id")
    val contentId: Int,
    @SerialName("language")
    val language: String,
    @SerialName("text")
    val text: String
)