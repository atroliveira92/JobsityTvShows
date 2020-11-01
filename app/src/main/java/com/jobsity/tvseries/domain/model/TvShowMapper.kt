package com.jobsity.tvseries.domain.model

import com.jobsity.tvseries.data.FavoritesTvShowEntity
import com.jobsity.tvseries.data.TvShowData
import com.jobsity.tvseries.data.TvShowEpisodeData
import com.jobsity.tvseries.data.TvShowSearchData

object TvShowMapper {
    fun mapTvShowData(tvShowsData: List<TvShowData>): List<TvShow> {
        return tvShowsData.filter {
            checkTvShow(it)

        }.map {
            convertToTvShow(it)
        }
    }

    fun mapTvShowSearchData(tvShowSearchData: List<TvShowSearchData>): List<TvShow> {
        return tvShowSearchData.filter {
            checkTvShow(it.show!!)

        }.map {
            convertToTvShow(it.show!!)
        }
    }


    fun mapFavoritesToTvShoList(favorites: List<FavoritesTvShowEntity>): List<TvShow> {
        return favorites.map {
            TvShow(
                it.id,
                it.name,
                it.posterHigh,
                it.posterMedium,
                it.time,
                it.days,
                it.genre,
                it.summary,
                it.rating,
                it.premier)
        }
    }

    fun mapEpisodesData(tvShowEpisodesData: List<TvShowEpisodeData>, tvShow: TvShow): List<TvShowEpisode> {
        return tvShowEpisodesData.filter {
            it.id != null
            && it.name != null
            && it.number != null
            && it.season != null

        }.map {
            TvShowEpisode(
                it.id!!,
                it.name!!,
                it.season!!,
                it.number!!,
                it.image?.medium?: tvShow.posterMedium,
                it.image?.original?: tvShow.posterHigh,
                it.summary?.replace("<p>", "")?.replace("</p>", "")
            )
        }
    }


    private fun checkTvShow(tvShowData: TvShowData): Boolean {
        return tvShowData.id != null
                && tvShowData.name != null
                && tvShowData.image != null
                && tvShowData.schedule != null
                && tvShowData.schedule!!.days != null
                && tvShowData.schedule!!.time != null
                && tvShowData.summary != null
                && tvShowData.premiered != null
    }

    private fun convertToTvShow(tvShowData: TvShowData): TvShow {
        val premierDate = getYearFromPremieredData(tvShowData.premiered)
        return TvShow(
            tvShowData.id!!,
            tvShowData.name!!,
            tvShowData.image?.original?: tvShowData.image!!.medium,
            tvShowData.image!!.medium!!,
            tvShowData.schedule!!.time!!,
            tvShowData.schedule!!.days!!,
            tvShowData.genres,
            tvShowData.summary!!.replace("<p>", "").replace("</p>", ""),
            tvShowData.rating?.average,
            premierDate
        )
    }

    private fun getYearFromPremieredData(premiered: String?): String? {
        if (premiered != null && premiered.contains("-")) {
            val split = premiered.split("-")
            if (split.isNotEmpty()) {
                return split[0]
            }
        }
        return null
    }
}