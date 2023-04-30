package com.juanarton.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.juanarton.core.data.api.APIResponse
import com.juanarton.core.data.api.video.MovieVideoResponse
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.remote.NetworkBoundRes
import com.juanarton.core.data.source.remote.RemoteDataSource
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.core.data.utils.DataMapper
import kotlinx.coroutines.flow.Flow

class TMDBRepository(private val remoteDataSource: RemoteDataSource): TMDBRepositoryInterface {

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

    override fun getMovieTrailer(id: String): Flow<Resource<List<Trailer>>> {
        return object : NetworkBoundRes<List<Trailer>, List<MovieVideoResponse>>() {
            override fun loadFromNetwork(data: List<MovieVideoResponse>): Flow<List<Trailer>> {
                val filteredData = data.filter {
                    it.site == "YouTube" && it.official == "true" && it.type == "Trailer"
                }
                return DataMapper.mapTrailerResponseToTrailerDomain(filteredData)
            }

            override suspend fun createCall(): Flow<APIResponse<List<MovieVideoResponse>>> {
                return remoteDataSource.getMovieTrailer(id.toInt())
            }
        }.asFlow()
    }
}