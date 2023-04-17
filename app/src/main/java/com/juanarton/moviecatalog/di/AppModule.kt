package com.juanarton.moviecatalog.di

import com.juanarton.core.data.domain.usecase.TMDBRepositoryInteractor
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase
import com.juanarton.moviecatalog.ui.fragments.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<TMDBRepositoryUseCase> {TMDBRepositoryInteractor(get())}
}

val viewModelModule = module {
    viewModel { HomeScreenViewModel(get()) }
}
