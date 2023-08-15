package com.juanarton.core.data.utils

import com.juanarton.core.data.api.movie.MovieDetailResponse
import com.juanarton.core.data.api.movie.PopularMovieResponse
import com.juanarton.core.data.api.search.SearchResponse
import com.juanarton.core.data.api.tvShow.PopularTvShowResponse
import com.juanarton.core.data.api.video.VideoTrailerResponse
import com.juanarton.core.data.domain.model.DetailMovie
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.source.local.room.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapTvShowResponseToMovieDomain(response: List<PopularTvShowResponse>): List<Movie>{
        val tvList = ArrayList<Movie>()
        response.map{
            val movie = Movie(
                it.id,
                it.backdropPath,
                it.overview,
                it.firstAirDate,
                it.name,
                it.voteAverage,
                it.voteCount,
                it.poster,
                Mode.TVSHOW.mode
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
                it.backdropPath,
                it.overview,
                it.releaseDate,
                it.title,
                it.voteAverage,
                it.voteCount,
                it.poster,
                Mode.MOVIE.mode
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

    fun mapSearchResponseToMovieDomain(response: List<SearchResponse>): List<Movie>{
        val search = ArrayList<Movie>()
        response.map {
            val title = if (it.title.isNullOrEmpty()) it.name else it.title
            val releaseDate = if (it.releaseDate.isNullOrEmpty()) it.firstAirDate else it.releaseDate

            if (!title.isNullOrEmpty() && !releaseDate.isNullOrEmpty()) {
                search.add(
                    Movie(
                        it.id,
                        it.backdropPath,
                        it.overview,
                        releaseDate,
                        title,
                        it.voteAverage,
                        it.voteCount,
                        it.poster,
                        it.mediaType
                    )
                )
            }
        }
        return search
    }

    fun mapSearchToMovieDomain(response: Movie): Movie {
        return Movie(
            response.id,
            response.backdropPath,
            response.overview,
            response.releaseDate,
            response.title,
            response.voteAverage,
            response.voteCount,
            response.poster,
            response.mediaType
        )
    }

    fun mapListEntityToDomain(list: List<FavoriteEntity>): List<Movie> =
        list.map {
            Movie(
                it.id,
                it.backdropPath,
                it.overview,
                it.releaseDate,
                it.title,
                it.voteAverage,
                it.voteCount,
                it.poster,
                it.mediaType
            )
        }

    fun mapDomainToEntity(movie: Movie) =
        FavoriteEntity (
            movie.id,
            movie.title,
            movie.releaseDate,
            movie.backdropPath,
            movie.overview,
            movie.poster,
            movie.voteAverage,
            movie.voteCount,
            movie.mediaType
        )

    fun mapEntityToDomain(favoriteEntity: FavoriteEntity) =
        Movie(
            favoriteEntity.id,
            favoriteEntity.backdropPath,
            favoriteEntity.overview,
            favoriteEntity.releaseDate,
            favoriteEntity.title,
            favoriteEntity.voteAverage,
            favoriteEntity.voteCount,
            favoriteEntity.poster,
            favoriteEntity.mediaType
        )
}