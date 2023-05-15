package com.juanarton.core.data.api.search

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TMDBSearchResponse(
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<SearchResponse>
)
