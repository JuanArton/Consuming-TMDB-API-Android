package com.juanarton.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("SELECT * FROM favorite")
    fun getListFavorite(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FavoriteEntity::class)
    fun insertMovieFavorite(movie: FavoriteEntity)

    @Query("SELECT EXISTS (SELECT * FROM favorite WHERE id = :movieId)")
    fun checkFavorite(movieId: String): Flow<Boolean>

    @Delete
    fun deleteFromFav(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getFavDetail(id: String): Flow<FavoriteEntity>

    @Update
    fun updateFavorite(favorite: FavoriteEntity)
}