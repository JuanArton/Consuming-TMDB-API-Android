package com.juanarton.core.data.source.remote

import android.util.Log
import com.example.core.data.api.PopularMovieResponse
import com.juanarton.core.data.api.API
import com.juanarton.core.data.api.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource() {
    suspend fun getPopularMovie(apiKey: String, language: String, page: Int): Flow<APIResponse<List<PopularMovieResponse>>> =
        flow{
            try{
                val recommendedMovie = API.services.getPopularMovie(apiKey, language, page).recomendedMovie
                if (recommendedMovie.isEmpty()){
                    emit(APIResponse.Error(null))
                }else{
                    emit(APIResponse.Success(recommendedMovie))
                }
            }catch (e: Exception){
                emit(APIResponse.Error(e.toString()))
                e.localizedMessage?.let { Log.e(RemoteDataSource::class.java.simpleName, it) }
            }
        }.flowOn(Dispatchers.IO)

}