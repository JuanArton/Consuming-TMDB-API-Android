package com.juanarton.core.data.source.local

import com.juanarton.core.data.source.local.room.DAO
import com.juanarton.core.data.source.local.room.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource (private val dao: DAO) {

    fun getListFavorite(): Flow<List<FavoriteEntity>> = dao.getListFavorite()

    fun insertMovieFavorite(movie: FavoriteEntity) = dao.insertMovieFavorite(movie)

    fun checkFavorite(movieId: String): Flow<Boolean> = dao.checkFavorite(movieId)

    fun deleteFromFav(favorite: FavoriteEntity) = dao.deleteFromFav(favorite)

    fun getFavDetail(id: String): Flow<FavoriteEntity> = dao.getFavDetail(id)

    fun updateFavorite(favorite: FavoriteEntity) = dao.updateFavorite(favorite)
}