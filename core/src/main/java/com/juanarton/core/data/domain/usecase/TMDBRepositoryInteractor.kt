package com.juanarton.core.data.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface

class TMDBRepositoryInteractor(private val tmdbRepository: TMDBRepositoryInterface): TMDBRepositoryUseCase {
    override fun getPopularMovie(): LiveData<PagingData<Movie>> = tmdbRepository.getPopularMovie()
}