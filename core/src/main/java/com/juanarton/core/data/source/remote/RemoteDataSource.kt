package com.juanarton.core.data.source.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.api.API
import com.juanarton.core.data.api.movie.PopularMovieResponse
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.utils.DataMapper

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
                    val response = mutableListOf<PopularMovieResponse>()
                    when(mode){
                        0 -> response.let { list1 ->
                            API.services.getPopularMovie(BuildConfig.API_KEY, "en", position).responseList.let (list1::addAll)
                        }
                        1 -> response.let { list1 ->
                            API.services.getPopularTvShow(BuildConfig.API_KEY, "en", position).responseList.let (list1::addAll)
                        }
                    }
                    val movie = DataMapper.mapMovieResponseToMovieDomain1(response)
                    LoadResult.Page(
                        data = movie,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = position + 1
                    )
                } catch (e: java.lang.Exception) {
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
}