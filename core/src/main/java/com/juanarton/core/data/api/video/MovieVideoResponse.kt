package com.juanarton.core.data.api.video

import com.google.gson.annotations.SerializedName

class MovieVideoResponse (
    @field:SerializedName("key")
    val key: String,

    @field:SerializedName("site")
    val site: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("official")
    val official: String
)