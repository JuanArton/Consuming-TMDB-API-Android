package com.juanarton.core.data.domain.usecase

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

interface TMDBRepositoryUseCase {
    fun getPopularMovie(): Flow<PagingData<Movie>>
    fun getPopularTvShow(): Flow<PagingData<Movie>>
    fun getMovieTrailer(id: String): Flow<Resource<List<Trailer>>>
}