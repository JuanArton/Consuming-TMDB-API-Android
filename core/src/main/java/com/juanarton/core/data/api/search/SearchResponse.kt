package com.juanarton.core.data.api.search

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("release_date")
    val releaseDate: String? = null,

    @field:SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Float? = null,

    @field:SerializedName("vote_count")
    val voteCount: String? = null,

    @field:SerializedName("poster_path")
    val poster: String? = null,

    @field:SerializedName("media_type")
    val mediaType: String
)
