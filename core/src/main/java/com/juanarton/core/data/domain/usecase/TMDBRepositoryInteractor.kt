package com.juanarton.core.data.domain.usecase

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Search
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

class TMDBRepositoryInteractor(private val tmdbRepository: TMDBRepositoryInterface): TMDBRepositoryUseCase {
    override fun getPopularMovie(): Flow<PagingData<Movie>> = tmdbRepository.getPopularMovie()
    override fun getPopularTvShow(): Flow<PagingData<Movie>> = tmdbRepository.getPopularTvShow()
    override fun getMovieTrailer(id: String, mode: String): Flow<Resource<List<Trailer>>> = tmdbRepository.getMovieTrailer(id, mode)
    override fun multiSearch(searchString: String): Flow<PagingData<Search>> = tmdbRepository.multiSearch(searchString)
}