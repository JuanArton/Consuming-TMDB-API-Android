package com.juanarton.core.data.utils

import com.juanarton.core.data.api.movie.PopularMovieResponse
import com.juanarton.core.data.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapMovieResponseToMovieDomain(response: List<PopularMovieResponse>): Flow<List<Movie>>{
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
                it.vote_count
            )
            movieList.add(movie)
        }
        return flowOf(movieList)
    }

    //Need to be renamed in the future
    fun mapMovieResponseToMovieDomain1(response: List<PopularMovieResponse>): List<Movie>{
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
                it.vote_count
            )
            movieList.add(movie)
        }
        return movieList
    }
}