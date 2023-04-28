package com.juanarton.moviecatalog.ui.fragments.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase
import com.juanarton.core.data.source.remote.Resource

class DetailMovieViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    private var id: MutableLiveData<String> = MutableLiveData()

    fun setID(id: String){
        this.id.value = id
    }

    fun movieTrailer(): LiveData<Resource<List<Trailer>>> = id.switchMap {
        tmdbRepositoryUseCase.getMovieTrailer(it).asLiveData()
    }
}