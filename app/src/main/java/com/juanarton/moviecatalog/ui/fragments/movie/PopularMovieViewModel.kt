package com.juanarton.moviecatalog.ui.fragments.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase

class PopularMovieViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    fun getPopularMovie(): LiveData<PagingData<Movie>> {
        return tmdbRepositoryUseCase.getPopularMovie().asLiveData().cachedIn(viewModelScope)
    }
}