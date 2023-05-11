package com.juanarton.core.data.api.search

import com.google.gson.annotations.SerializedName

data class TMDBSearchResponse(
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<SearchResponse>
)
