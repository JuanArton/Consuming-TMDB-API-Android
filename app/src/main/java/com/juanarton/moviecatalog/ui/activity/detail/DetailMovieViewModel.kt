package com.juanarton.moviecatalog.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.juanarton.core.data.domain.model.Credit
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.domain.model.Trailer
import com.juanarton.core.data.domain.usecase.TMDBRepositoryUseCase
import com.juanarton.core.data.source.remote.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailMovieViewModel(private val tmdbRepositoryUseCase: TMDBRepositoryUseCase): ViewModel() {
    private var movie: MutableLiveData<Movie> = MutableLiveData()
    var _isFav: MutableLiveData<Boolean> = MutableLiveData(false)
    var isFav: LiveData<Boolean> = _isFav

    val movieDetail = movie.switchMap {
        tmdbRepositoryUseCase.getMovieDetail(it.id).asLiveData()
    }

    fun setProperty(id: Movie){
        this.movie.value = id
    }

    fun setFav(favStat: Boolean){
        _isFav.value = favStat
    }

    fun movieTrailer(): LiveData<Resource<List<Trailer>>> = movie.switchMap {
        tmdbRepositoryUseCase.getMovieTrailer(it.id, it.mediaType).asLiveData()
    }

    fun movieCredit(): LiveData<Resource<List<Credit>>> = movie.switchMap {
        tmdbRepositoryUseCase.getCreditList(it.id).asLiveData()
    }

    fun insertMovieFavorite(movie: Movie) = CoroutineScope(Dispatchers.IO).launch {
        tmdbRepositoryUseCase.insertMovieFavorite(movie)
    }
    fun checkFavorite() {
        movie.value?.let {
            isFav = tmdbRepositoryUseCase.checkFavorite(it.id).asLiveData()
        }
    }

    fun deleteFromFav(movie: Movie) = CoroutineScope(Dispatchers.IO).launch {
        tmdbRepositoryUseCase.deleteFromFav(movie)
    }
}