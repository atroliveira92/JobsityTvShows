package com.jobsity.tvseries.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jobsity.tvseries.data.FavoritesTvShowEntity

@Dao
interface FavoritesTvShowDao {

    @Query("SELECT * FROM FavoritesTVShow")
    suspend fun loadAll(): List<FavoritesTvShowEntity>

    @Query("SELECT * FROM FavoritesTVShow WHERE id = :id")
    suspend fun loadFromId(id: Int): FavoritesTvShowEntity?

    @Insert
    suspend fun insert(favoritesTvShowEntity: FavoritesTvShowEntity)

    @Delete
    suspend fun delete(favoritesTvShowEntity: FavoritesTvShowEntity)
}