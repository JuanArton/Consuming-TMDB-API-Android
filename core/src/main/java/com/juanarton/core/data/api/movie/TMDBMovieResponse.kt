package com.juanarton.core.data.api.movie

import com.google.gson.annotations.SerializedName


data class TMDBMovieResponse (
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val responseList: List<PopularMovieResponse>
)