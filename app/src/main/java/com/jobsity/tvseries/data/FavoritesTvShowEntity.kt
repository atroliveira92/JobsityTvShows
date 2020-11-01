package com.jobsity.tvseries.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jobsity.tvseries.domain.model.TvShow

@Entity(tableName = "FavoritesTVShow")
data class FavoritesTvShowEntity (
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val posterHigh: String?,
    val posterMedium: String,
    val time: String,
    val days: List<String>,
    val genre: List<String>?,
    val summary: String,
    val rating: String?,
    val premier: String?
)