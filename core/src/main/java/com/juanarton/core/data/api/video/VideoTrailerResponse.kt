package com.juanarton.core.data.api.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class VideoTrailerResponse (
    @field:SerializedName("key")
    val key: String,

    @field:SerializedName("site")
    val site: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("official")
    val official: String
)