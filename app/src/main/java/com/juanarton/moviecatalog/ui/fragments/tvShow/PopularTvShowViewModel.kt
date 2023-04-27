package com.juanarton.moviecatalog.ui.fragments.tvShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase
import com.juanarton.core.data.repository.TMDBRepository

class PopularTvShowViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    fun getPopularTvShow(): LiveData<PagingData<Movie>> {
        return tmdbRepositoryUseCase.getPopularTvShow().asLiveData().cachedIn(viewModelScope)
    }
}