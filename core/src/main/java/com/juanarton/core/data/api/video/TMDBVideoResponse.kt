package com.juanarton.core.data.api.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TMDBVideoResponse (
    @field:SerializedName("id")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<VideoTrailerResponse>
)