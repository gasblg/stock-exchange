package com.github.gasblg.models.languages
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageRequest(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
 )