package com.juanarton.core.data.api.movie

import com.google.gson.annotations.SerializedName

data class PopularMovieResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("backdrop_path")
    val backdrop_path: String? = null,

    @field:SerializedName("genre_ids")
    val genre_ids: List<String>? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("release_date")
    val release_date: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("vote_average")
    val vote_average: String? = null,

    @field:SerializedName("vote_count")
    val vote_count: String? = null,

    @field:SerializedName("poster_path")
    val poster: String? = null
)
