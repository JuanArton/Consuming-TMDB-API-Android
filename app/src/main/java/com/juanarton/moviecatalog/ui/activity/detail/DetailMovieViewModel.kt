package com.juanarton.moviecatalog.ui.activity.detail

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
    private var mode = ""

    fun setProperty(id: String, mode: String){
        this.id.value = id
        this.mode = mode
    }

    fun movieTrailer(): LiveData<Resource<List<Trailer>>> = id.switchMap {
        tmdbRepositoryUseCase.getMovieTrailer(it, mode).asLiveData()
    }
}