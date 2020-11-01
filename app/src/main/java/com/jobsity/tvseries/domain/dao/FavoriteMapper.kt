package com.jobsity.tvseries.domain.dao

import com.jobsity.tvseries.data.FavoritesTvShowEntity
import com.jobsity.tvseries.domain.model.TvShow

object FavoriteMapper {

    fun tvShowToFavorite(tvShow: TvShow): FavoritesTvShowEntity {
        return FavoritesTvShowEntity(
            tvShow.id,
            tvShow.name,
            tvShow.posterHigh,
            tvShow.posterMedium,
            tvShow.time,
            tvShow.days,
            tvShow.genre,
            tvShow.summary,
            tvShow.rating,
            tvShow.premier)
    }
}