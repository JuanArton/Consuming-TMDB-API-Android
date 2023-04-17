package com.juanarton.core.di

import com.juanarton.core.data.domain.repository.TMDBRepositoryInterface
import com.juanarton.core.data.repository.TMDBRepository
import com.juanarton.core.data.source.remote.RemoteDataSource
import org.koin.dsl.module

val repositoryModule = module {
    single { RemoteDataSource() }
    single<TMDBRepositoryInterface> {
        TMDBRepository(
            get()
        )
    }
}
