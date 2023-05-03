package com.juanarton.core.data.api.video

import com.google.gson.annotations.SerializedName

data class TMDBVideoResponse (
    @field:SerializedName("id")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<VideoTrailerResponse>
)