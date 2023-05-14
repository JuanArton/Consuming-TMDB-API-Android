package com.juanarton.favorite.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase

class FavoriteViewModel (private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    fun getListFavorite(): LiveData<List<Movie>> {
        return tmdbRepositoryUseCase.getListFavorite().asLiveData()
    }
}