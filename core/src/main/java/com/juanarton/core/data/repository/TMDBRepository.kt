package com.juanarton.core.data.repository

import android.util.Log
import com.example.core.data.api.PopularMovieResponse
import com.juanarton.core.data.source.remote.RemoteDataSource
import com.juanarton.core.data.api.APIResponse
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.remote.NetworkBoundRes
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.core.data.utils.DataMapper
import kotlinx.coroutines.flow.Flow

class TMDBRepository(private val remoteDataSource: RemoteDataSource): TMDBRepositoryInterface {

    override fun getPopularMovie(
        apiKey: String,
        language: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return object : NetworkBoundRes<List<Movie>, List<PopularMovieResponse>>(){
            override fun loadFromNetwork(data: List<PopularMovieResponse>): Flow<List<Movie>>{
                Log.d("daftar movie", data.toString())
                return DataMapper.mapMovieResponseToMovieDomain(data)
            }

            override suspend fun  createCall(): Flow<APIResponse<List<PopularMovieResponse>>>{
                return remoteDataSource.getPopularMovie(apiKey, language, page)
            }
        }.asFlow()
    }

}