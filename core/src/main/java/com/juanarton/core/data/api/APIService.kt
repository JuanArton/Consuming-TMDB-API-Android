package com.juanarton.core.data.api

import com.juanarton.core.data.api.credits.TMDBCreditsResponse
import com.juanarton.core.data.api.movie.MovieDetailResponse
import com.juanarton.core.data.api.movie.TMDBMovieResponse
import com.juanarton.core.data.api.search.TMDBSearchResponse
import com.juanarton.core.data.api.tvShow.TMDBTvShowResponse
import com.juanarton.core.data.api.video.TMDBVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("3/movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): TMDBMovieResponse

    @GET("3/tv/popular")
    suspend fun getPopularTvShow(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): TMDBTvShowResponse

    @GET("3/" + "{mode}/" + "{movie_id}" + "/videos")
    suspend fun getMovieVideo(
        @Path("mode")mode: String,
        @Path("movie_id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): TMDBVideoResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String,
    ): MovieDetailResponse

    @GET("/3/search/multi")
    suspend fun multiSearch(
        @Query("query") searchString: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): TMDBSearchResponse

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String,
    ): TMDBCreditsResponse
}