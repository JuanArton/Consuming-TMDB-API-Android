package com.juanarton.core.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailMovie(
    val runtime: String? = null,
    val genres: String? = null,
): Parcelable
