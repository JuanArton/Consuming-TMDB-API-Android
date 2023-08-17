package com.juanarton.core.data.api.credits

import com.google.gson.annotations.SerializedName

data class TMDBCreditsResponse (
    @field:SerializedName("cast")
    val cast: List<CreditsResponse>
)

data class CreditsResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("profile_path")
    val profilePath: String? = null,

    @field:SerializedName("character")
    val character: String? = null,
)