package com.juanarton.core.data.api

import com.juanarton.core.data.api.movie.TMDBMovieResponse
import com.juanarton.core.data.api.search.TMDBSearchResponse
import com.juanarton.core.data.api.tvShow.TMDBTvShowResponse
import com.juanarton.core.data.api.video.TMDBVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/popular")
    suspend fun getPopularMovie(
        @Query("mode") mode: String? = "movie",
        @Query("language") language: String,
        @Query("page") page: Int
    ): TMDBMovieResponse

    @GET("/popular")
    suspend fun getPopularTvShow(
        @Query("mode") mode: String? = "tv",
        @Query("language") language: String,
        @Query("page") page: Int
    ): TMDBTvShowResponse

    @GET("/trailer/" + "{movie_id}")
    suspend fun getMovieVideo(
        @Path("mode") mode: String,
        @Path("movie_id") id: Int,
        @Query("language") language: String
    ): TMDBVideoResponse

    @GET("/search/multi")
    suspend fun multiSearch(
        @Query("query") searchString: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int
    ): TMDBSearchResponse
}