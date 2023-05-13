package com.juanarton.moviecatalog.ui.fragments.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase

class SearchViewModel (private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    private var _searchString: MutableLiveData<String> = MutableLiveData()
    private var searchString: LiveData<String> = _searchString

    var movieSearchList: LiveData<PagingData<Movie>> = searchString.switchMap {
        tmdbRepositoryUseCase.multiSearch(it).asLiveData().cachedIn(viewModelScope)
    }

    fun setSearchString(searchString: String){
        this._searchString.value = searchString
    }
}