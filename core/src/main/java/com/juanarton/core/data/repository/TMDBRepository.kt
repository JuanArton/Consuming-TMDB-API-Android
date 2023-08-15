package com.juanarton.core.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.juanarton.core.data.api.APIResponse
import com.juanarton.core.data.api.movie.MovieDetailResponse
import com.juanarton.core.data.api.video.VideoTrailerResponse
import com.juanarton.core.data.domain.model.DetailMovie
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.local.LocalDataSource
import com.juanarton.core.data.source.local.room.FavoriteEntity
import com.juanarton.core.data.source.remote.NetworkBoundRes
import com.juanarton.core.data.source.remote.RemoteDataSource
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.core.data.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TMDBRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): TMDBRepositoryInterface {

    override fun getPopularMovie(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                remoteDataSource.getPopularMovie()
            }, initialKey = 1
        ).flow
    }

    override fun getPopularTvShow(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                remoteDataSource.getPopularTvShow()
            },
            initialKey = 1
        ).flow
    }

    override fun getMovieTrailer(id: String, mode: String): Flow<Resource<List<Trailer>>> {
        return object : NetworkBoundRes<List<Trailer>, List<VideoTrailerResponse>>() {
            override fun loadFromNetwork(data: List<VideoTrailerResponse>): Flow<List<Trailer>> {
                val filteredData = data.filter {
                    it.site == "YouTube" && it.official == "true" && it.type == "Trailer"
                }
                return DataMapper.mapTrailerResponseToTrailerDomain(filteredData)
            }

            override suspend fun createCall(): Flow<APIResponse<List<VideoTrailerResponse>>> {
                return remoteDataSource.getMovieTrailer(id.toInt(), mode)
            }
        }.asFlow()
    }

    override fun multiSearch(searchString: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                remoteDataSource.multiSearch(searchString)
            },
            initialKey = 1
        ).flow
    }

    override fun getMovieDetail(id: String): Flow<Resource<DetailMovie>> {
        return object : NetworkBoundRes<DetailMovie, MovieDetailResponse>() {
            override fun loadFromNetwork(data: MovieDetailResponse): Flow<DetailMovie> {
                val genres = data.genres?.map { it.name }?.joinToString(separator = ", ")
                val runtime = data.runtime?.let {
                    if (it > 60) {
                        val hours = it / 60
                        val remainingMinutes = it % 60
                        String.format("%02d hours %02d minutes", hours, remainingMinutes)
                    } else {
                        String.format("%02d", it)
                    }
                }
                return flowOf(
                    DetailMovie(
                        runtime,
                        genres
                    )
                )
            }

            override suspend fun createCall(): Flow<APIResponse<MovieDetailResponse>> {
                return remoteDataSource.getMovieDetail(id.toInt())
            }
        }.asFlow()
    }

    override fun getListFavorite(): Flow<List<Movie>> {
        return localDataSource.getListFavorite().map {
            DataMapper.mapListEntityToDomain(it)
        }
    }

    override suspend fun insertMovieFavorite(movie: Movie) {
        return localDataSource.insertMovieFavorite(
            DataMapper.mapDomainToEntity(movie)
        )
    }

    override fun checkFavorite(movieId: String): Flow<Boolean> {
        return localDataSource.checkFavorite(movieId)
    }

    override fun deleteFromFav(movie: Movie) {
        localDataSource.deleteFromFav(
            DataMapper.mapDomainToEntity(movie)
        )
    }

    override fun getFavDetail(id: String): Flow<FavoriteEntity> {
        TODO("Not yet implemented")
    }

    override fun updateFavorite(movie: Movie) {
        localDataSource.updateFavorite(
            DataMapper.mapDomainToEntity(movie)
        )
    }
}