package com.juanarton.core.data.api.movie

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MovieDetailResponse (
    @field:SerializedName("runtime")
    val runtime: Int? = null,

    @field:SerializedName("genres")
    val genres: List<MovieGenres>? = null
)

data class MovieGenres (
    @field:SerializedName("name")
    val name: String? = null
)