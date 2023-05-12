package com.juanarton.core.data.api.tvShow

import com.google.gson.annotations.SerializedName

class PopularTvShowResponse (
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Float? = null,

    @field:SerializedName("vote_count")
    val voteCount: String? = null,

    @field:SerializedName("poster_path")
    val poster: String? = null
)