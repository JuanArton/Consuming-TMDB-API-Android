package com.juanarton.core.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: String,
    val backdrop_path: String,
    val genre_ids: List<String>,
    val overview: String,
    val release_date: String,
    val title: String,
    val vote_average: String,
    val vote_count: String
): Parcelable