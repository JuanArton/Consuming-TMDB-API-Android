package com.example.core.data.api

import com.google.gson.annotations.SerializedName

data class PopularMovieResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("backdrop_path")
    val backdrop_path: String,

    @field:SerializedName("genre_ids")
    val genre_ids: List<String>,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("release_date")
    val release_date: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("vote_average")
    val vote_average: String,

    @field:SerializedName("vote_count")
    val vote_count: String
)
