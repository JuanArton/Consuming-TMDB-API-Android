package com.juanarton.core.data.api

import com.google.gson.annotations.SerializedName


data class TMDBAPIResponse (
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val popularMovie: List<PopularMovieResponse>
)