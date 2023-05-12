package com.juanarton.core.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: String = "",
    val backdropPath: String? = null,
    val overview: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val voteAverage: Float? = null,
    val voteCount: String? = null,
    val poster: String? = null,
    val mediaType: String? = null
): Parcelable
