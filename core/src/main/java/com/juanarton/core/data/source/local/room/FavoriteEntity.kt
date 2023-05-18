package com.juanarton.core.data.source.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String? = "",

    @ColumnInfo(name = "release_date")
    val releaseDate: String? = "",

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String? = "",

    @ColumnInfo(name = "overview")
    val overview: String? = "",

    @ColumnInfo(name = "poster")
    val poster: String? = "",

    @ColumnInfo(name = "vote_average")
    val voteAverage: Float? = null,

    @ColumnInfo(name = "vote_count")
    val voteCount: String? = "",

    @ColumnInfo(name = "media_type")
    val mediaType: String
)