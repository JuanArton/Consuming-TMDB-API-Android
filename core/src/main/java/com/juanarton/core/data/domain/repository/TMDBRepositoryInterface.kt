package com.juanarton.core.data.domain.repository

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Search
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

interface TMDBRepositoryInterface {
    fun getPopularMovie(): Flow<PagingData<Movie>>
    fun getPopularTvShow(): Flow<PagingData<Movie>>
    fun getMovieTrailer(id: String, mode: String): Flow<Resource<List<Trailer>>>
    fun multiSearch(searchString: String): Flow<PagingData<Search>>
}