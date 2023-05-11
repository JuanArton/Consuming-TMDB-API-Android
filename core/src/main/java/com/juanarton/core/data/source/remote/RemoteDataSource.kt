package com.juanarton.core.data.source.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.api.API
import com.juanarton.core.data.api.APIResponse
import com.juanarton.core.data.api.video.VideoTrailerResponse
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Search
import com.juanarton.core.data.utils.DataMapper
import com.juanarton.core.data.utils.Mode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource{

    fun getPopularMovie(): PagingSource<Int, Movie> {
        return getList(0)
    }

    fun getPopularTvShow(): PagingSource<Int, Movie> {
        return getList(1)
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

    fun multiSearch(searchString: String): PagingSource<Int, Search>{
        return object : PagingSource<Int, Search>(){
            override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
                return try {
                    Log.d("pagingtest", "load")
                    val position = params.key ?: 1
                    val searchResult = API.services.multiSearch(searchString, true, BuildConfig.API_KEY, "en", position).responseList
                    val mappedResult = DataMapper.mapSearchResponseToMovieDomain(searchResult)
                    LoadResult.Page(
                        data = mappedResult,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (mappedResult.isNotEmpty()) position+1 else null
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

        }
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
                        nextKey = if (movie.isNotEmpty()) position+1 else null
                    )
                } catch (e: Exception) {
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

}