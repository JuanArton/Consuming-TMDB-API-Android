package com.juanarton.core.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    val id: String,
    val backdrop_path: String? = null,
    val genre_ids: List<String>? = null,
    val overview: String? = null,
    val release_date: String? = null,
    val title: String? = null,
    val vote_average: Float? = null,
    val vote_count: String? = null,
    val poster: String? = null,
    val media_type: String? = null
): Parcelable
