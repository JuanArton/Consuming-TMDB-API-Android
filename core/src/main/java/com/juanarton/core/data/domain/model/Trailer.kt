package com.juanarton.core.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailer (
    val key: String,
    val site: String,
    val type: String,
    val official: String
): Parcelable