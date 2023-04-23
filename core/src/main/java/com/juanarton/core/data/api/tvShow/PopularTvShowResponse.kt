package com.juanarton.core.data.api.tvShow

import com.google.gson.annotations.SerializedName

class PopularTvShowResponse (
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("backdrop_path")
    val backdrop_path: String? = null,

    @field:SerializedName("genre_ids")
    val genre_ids: List<String>? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("first_air_date")
    val first_air_date: String? = null,

    @field:SerializedName("title")
    val name: String? = null,

    @field:SerializedName("vote_average")
    val vote_average: String? = null,

    @field:SerializedName("vote_count")
    val vote_count: String? = null
)