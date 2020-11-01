package com.jobsity.tvseries.domain.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jobsity.tvseries.data.FavoritesTvShowEntity

@Database(
    entities = [FavoritesTvShowEntity::class],
    version = 1, exportSchema = false
)

@TypeConverters(Converter::class)
abstract class JobsityDatabase : RoomDatabase() {
    abstract val favoritesTvShowDao: FavoritesTvShowDao
}