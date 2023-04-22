package com.juanarton.core.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.source.remote.RemoteDataSource

class TMDBRepository(private val remoteDataSource: RemoteDataSource): TMDBRepositoryInterface {

    override fun getPopularMovie(): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                remoteDataSource.getPopularMovie()
            }
            , initialKey = 1
        ).liveData
    }
}