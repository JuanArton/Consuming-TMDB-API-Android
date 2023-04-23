package com.juanarton.moviecatalog.ui.fragments.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase

class HomeScreenViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    fun getPopularMovie(): LiveData<PagingData<Movie>> {
        return tmdbRepositoryUseCase.getPopularMovie()
    }
}