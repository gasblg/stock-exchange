package com.github.gasblg.models.translations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequest(
    @SerialName("content_id")
    val contentId: Int,
    @SerialName("language")
    val language: String,
    @SerialName("text")
    val text: String
)