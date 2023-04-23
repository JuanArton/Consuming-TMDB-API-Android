package com.juanarton.core.data.api

import com.juanarton.core.data.api.movie.TMDBMovieResponse
import com.juanarton.core.data.api.tvShow.TMDBTvShowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("3/movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") key: String,
        @Query("language") username: String,
        @Query("page") page: Int
    ): TMDBMovieResponse

    @GET("3/tv/popular")
    suspend fun getPopularTvShow(
        @Query("api_key") key: String,
        @Query("language") username: String,
        @Query("page") page: Int
    ): TMDBTvShowResponse
}