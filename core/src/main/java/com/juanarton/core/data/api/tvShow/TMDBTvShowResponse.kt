package com.juanarton.core.data.api.tvShow

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.juanarton.core.data.api.movie.PopularMovieResponse

@Keep
data class TMDBTvShowResponse (
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<PopularTvShowResponse>
)