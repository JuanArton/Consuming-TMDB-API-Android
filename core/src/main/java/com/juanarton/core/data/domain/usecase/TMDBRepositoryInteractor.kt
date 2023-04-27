package com.juanarton.core.data.domain.usecase

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import kotlinx.coroutines.flow.Flow

class TMDBRepositoryInteractor(private val tmdbRepository: TMDBRepositoryInterface): TMDBRepositoryUseCase {
    override fun getPopularMovie(): Flow<PagingData<Movie>> = tmdbRepository.getPopularMovie()
    override fun getPopularTvShow(): Flow<PagingData<Movie>> = tmdbRepository.getPopularTvShow()
}