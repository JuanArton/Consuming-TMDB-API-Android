package com.juanarton.core.data.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie

interface TMDBRepositoryInterface {
    fun getPopularMovie(): LiveData<PagingData<Movie>>
}