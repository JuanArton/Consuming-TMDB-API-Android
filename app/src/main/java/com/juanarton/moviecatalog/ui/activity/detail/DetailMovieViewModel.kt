package com.juanarton.moviecatalog.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailMovieViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    private var id: MutableLiveData<String> = MutableLiveData()
    private var mode: MutableLiveData<String> = MutableLiveData()
    var _isFav: MutableLiveData<Boolean> = MutableLiveData(false)
    var isFav: LiveData<Boolean> = _isFav

    fun setProperty(id: String, mode: String){
        this.id.value = id
        this.mode.value = mode
    }

    fun setFav(favStat: Boolean){
        _isFav.value = favStat
    }

    fun movieTrailer(): LiveData<Resource<List<Trailer>>> = id.switchMap {
        mode.switchMap { mode ->
            tmdbRepositoryUseCase.getMovieTrailer(it, mode).asLiveData()
        }
    }

    fun insertMovieFavorite(movie: Movie) = CoroutineScope(Dispatchers.IO).launch {
        tmdbRepositoryUseCase.insertMovieFavorite(movie)
    }
    fun checkFavorite() {
        id.value?.let {
            isFav = tmdbRepositoryUseCase.checkFavorite(it).asLiveData()
        }
    }

    fun deleteFromFav(movie: Movie) = CoroutineScope(Dispatchers.IO).launch {
        tmdbRepositoryUseCase.deleteFromFav(movie)
    }
}