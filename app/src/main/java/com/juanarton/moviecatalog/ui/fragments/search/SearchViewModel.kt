package com.juanarton.moviecatalog.ui.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Search
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase
import com.juanarton.core.data.source.remote.Resource

class SearchViewModel (private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    private var searchString = ""

    fun setSearchString(searchString: String){
        this.searchString = searchString
    }

    fun multiSearch(): LiveData<PagingData<Search>> =
        tmdbRepositoryUseCase.multiSearch(searchString).asLiveData().cachedIn(viewModelScope)

}