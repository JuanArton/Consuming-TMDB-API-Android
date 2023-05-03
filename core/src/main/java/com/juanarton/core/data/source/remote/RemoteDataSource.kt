package com.juanarton.core.data.source.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.api.API
import com.juanarton.core.data.api.APIResponse
import com.juanarton.core.data.api.video.VideoTrailerResponse
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.utils.DataMapper
import com.juanarton.core.data.utils.Mode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class RemoteDataSource{

    fun getPopularMovie(): PagingSource<Int, Movie> {
        return getList(0)
    }

    fun getPopularTvShow(): PagingSource<Int, Movie> {
        return getList(1)
    }

    // variable mode value 1 means should fetch tvShow Popular list, value 0 means movie
    private fun getList(mode: Int): PagingSource<Int, Movie>{
        return object : PagingSource<Int, Movie>(){
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
                return try {
                    val position = params.key ?: 1
                    val movie = mutableListOf<Movie>()
                    when(mode){
                        0 -> movie.let { popularList ->
                            DataMapper.mapMovieResponseToMovieDomain(
                                API.services.getPopularMovie(BuildConfig.API_KEY, "en", position).responseList
                            ).let (popularList::addAll)
                        }
                        1 -> movie.let { popularList ->
                            DataMapper.mapTvShowResponseToMovieDomain(
                                API.services.getPopularTvShow(BuildConfig.API_KEY, "en", position).responseList
                            ).let (popularList::addAll)
                        }
                    }
                    Log.d("MovieData", movie.toString())
                    LoadResult.Page(
                        data = movie,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = position + 1
                    )
                } catch (e: Exception) {
                    Log.d("Pagingtest", e.toString())
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }
    }

    suspend fun getMovieTrailer(id: Int, mode: String): Flow<APIResponse<List<VideoTrailerResponse>>> =
        flow{
            try{
                val trailerList = getTrailer(id, mode)
                if (trailerList.isEmpty()){
                    emit(APIResponse.Error(null))
                } else{
                    emit(APIResponse.Success(trailerList))
                }
            } catch (e: Exception){
                emit(APIResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getTrailer(id: Int, mode: String): List<VideoTrailerResponse>{
        val trailer = when(mode){
            Mode.TVSHOW.mode -> API.services.getMovieVideo(Mode.TVSHOW.mode, id, BuildConfig.API_KEY, "en")
            else -> {
                API.services.getMovieVideo(Mode.MOVIE.mode, id, BuildConfig.API_KEY, "en")
            }
        }
        return trailer.responseList
    }
}