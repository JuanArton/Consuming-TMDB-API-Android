package com.juanarton.core.data.api.tvShow

import com.google.gson.annotations.SerializedName
import com.juanarton.core.data.api.movie.PopularMovieResponse

data class TMDBTvShowResponse (
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<PopularTvShowResponse>
)