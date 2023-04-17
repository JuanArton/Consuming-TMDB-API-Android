package com.juanarton.moviecatalog.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase

class HomeScreenViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    fun getPopularMovie(apiKey: String, language: String, page: Int) =
        tmdbRepositoryUseCase.getPopularMovie(apiKey, language, page).asLiveData()
}