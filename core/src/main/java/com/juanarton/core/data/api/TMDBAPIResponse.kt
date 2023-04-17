package com.juanarton.core.data.api

import com.example.core.data.api.PopularMovieResponse
import com.google.gson.annotations.SerializedName


data class TMDBAPIResponse (
    @field:SerializedName("results")
    val recomendedMovie: List<PopularMovieResponse>
)