package com.juanarton.moviecatalog.ui.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase

class SearchViewModel (private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    private var searchString = ""

    fun setSearchString(searchString: String){
        this.searchString = searchString
    }

    fun multiSearch(): LiveData<PagingData<Movie>> =
        tmdbRepositoryUseCase.multiSearch(searchString).asLiveData().cachedIn(viewModelScope)

}