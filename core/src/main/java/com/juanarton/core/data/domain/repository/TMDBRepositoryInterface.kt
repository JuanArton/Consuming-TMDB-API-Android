package com.juanarton.core.data.domain.repository

import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

interface TMDBRepositoryInterface {
    fun getPopularMovie(apiKey: String, language: String, page: Int): Flow<Resource<List<Movie>>>
}