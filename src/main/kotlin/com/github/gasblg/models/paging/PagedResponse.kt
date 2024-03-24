package com.github.gasblg.models.paging

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
    @SerialName("count")
    val count: Long? = null,
    @SerialName("next")
    val next: Int? = null,
    @SerialName("previous")
    val previous: Int? = null,
    @SerialName("results")
    val results: List<T> = listOf()

)
