package com.juanarton.core.data.domain.repository

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface TMDBRepositoryInterface {
    fun getPopularMovie(): Flow<PagingData<Movie>>
    fun getPopularTvShow(): Flow<PagingData<Movie>>
}