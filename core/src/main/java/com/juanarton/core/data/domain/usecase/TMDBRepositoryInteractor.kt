package com.juanarton.core.data.domain.usecase

import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.local.room.FavoriteEntity
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

class TMDBRepositoryInteractor(private val tmdbRepository: TMDBRepositoryInterface): TMDBRepositoryUseCase {
    override fun getPopularMovie(): Flow<PagingData<Movie>> = tmdbRepository.getPopularMovie()
    override fun getPopularTvShow(): Flow<PagingData<Movie>> = tmdbRepository.getPopularTvShow()
    override fun getMovieTrailer(id: String, mode: String): Flow<Resource<List<Trailer>>> = tmdbRepository.getMovieTrailer(id, mode)
    override fun multiSearch(searchString: String): Flow<PagingData<Movie>> = tmdbRepository.multiSearch(searchString)
    override fun getListFavorite(): Flow<List<Movie>>  = tmdbRepository.getListFavorite()
    override suspend fun insertMovieFavorite(movie: Movie) = tmdbRepository.insertMovieFavorite(movie)
    override fun checkFavorite(movieId: String): Flow<Boolean> = tmdbRepository.checkFavorite(movieId)
    override fun deleteFromFav(movie: Movie) = tmdbRepository.deleteFromFav(movie)
    override fun getFavDetail(id: String): Flow<FavoriteEntity> {
        TODO("Not yet implemented")
    }
    override fun updateFavorite(movie: Movie) = tmdbRepository.updateFavorite(movie)
}