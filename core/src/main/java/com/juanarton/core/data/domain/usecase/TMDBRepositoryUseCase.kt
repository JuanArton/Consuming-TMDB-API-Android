package com.juanarton.core.data.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie

interface TMDBRepositoryUseCase {
    fun getPopularMovie(): LiveData<PagingData<Movie>>
}