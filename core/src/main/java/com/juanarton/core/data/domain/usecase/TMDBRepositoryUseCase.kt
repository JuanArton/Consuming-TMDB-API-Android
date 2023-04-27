package com.juanarton.core.data.domain.usecase

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface TMDBRepositoryUseCase {
    fun getPopularMovie(): Flow<PagingData<Movie>>
    fun getPopularTvShow(): Flow<PagingData<Movie>>
}