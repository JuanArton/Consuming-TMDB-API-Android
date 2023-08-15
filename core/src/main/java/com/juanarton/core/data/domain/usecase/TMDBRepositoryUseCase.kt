package com.juanarton.core.data.domain.usecase

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.DetailMovie
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.local.room.FavoriteEntity
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

interface TMDBRepositoryUseCase {
    fun getPopularMovie(): Flow<PagingData<Movie>>
    fun getPopularTvShow(): Flow<PagingData<Movie>>
    fun getMovieTrailer(id: String, mode: String): Flow<Resource<List<Trailer>>>
    fun multiSearch(searchString: String): Flow<PagingData<Movie>>
    fun getListFavorite(): Flow<List<Movie>>
    suspend fun insertMovieFavorite(movie: Movie)
    fun checkFavorite(movieId: String): Flow<Boolean>
    fun deleteFromFav(movie: Movie)
    fun getFavDetail(id: String): Flow<FavoriteEntity>
    fun updateFavorite(movie: Movie)
    fun getMovieDetail(id: String): Flow<Resource<DetailMovie>>
}