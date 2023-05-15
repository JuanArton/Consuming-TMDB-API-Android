package com.juanarton.core.data.api.movie

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TMDBMovieResponse (
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<PopularMovieResponse>
)