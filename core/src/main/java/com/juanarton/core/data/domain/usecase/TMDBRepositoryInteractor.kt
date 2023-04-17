package com.juanarton.core.data.domain.usecase

import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

class TMDBRepositoryInteractor(private val tmdbRepository: TMDBRepositoryInterface): TMDBRepositoryUseCase {
    override fun getPopularMovie(
        apiKey: String,
        language: String,
        page: Int
    ): Flow<Resource<List<Movie>>> = tmdbRepository.getPopularMovie(apiKey, language, page)
}