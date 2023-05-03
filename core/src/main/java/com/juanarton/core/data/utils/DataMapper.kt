package com.juanarton.core.data.utils

import com.juanarton.core.data.api.movie.PopularMovieResponse
import com.juanarton.core.data.api.tvShow.PopularTvShowResponse
import com.juanarton.core.data.api.video.VideoTrailerResponse
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapTvShowResponseToMovieDomain(response: List<PopularTvShowResponse>): List<Movie>{
        val tvList = ArrayList<Movie>()
        response.map{
            val movie = Movie(
                it.id,
                it.backdrop_path,
                it.genre_ids,
                it.overview,
                it.first_air_date,
                it.name,
                it.vote_average,
                it.vote_count,
                it.poster
            )
            tvList.add(movie)
        }
        return tvList
    }

    //Need to be renamed in the future
    fun mapMovieResponseToMovieDomain(response: List<PopularMovieResponse>): List<Movie>{
        val movieList = ArrayList<Movie>()
        response.map{
            val movie = Movie(
                it.id,
                it.backdrop_path,
                it.genre_ids,
                it.overview,
                it.release_date,
                it.title,
                it.vote_average,
                it.vote_count,
                it.poster
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapTrailerResponseToTrailerDomain(response: List<VideoTrailerResponse>): Flow<List<Trailer>> {
        val trailerList = ArrayList<Trailer>()
        response.map {
            val trailer = Trailer(
                it.key,
                it.site,
                it.type,
                it.official
            )
            trailerList.add(trailer)
        }
        return flowOf(trailerList)
    }
}