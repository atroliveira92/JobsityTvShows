package com.jobsity.tvseries.domain.model

import com.jobsity.tvseries.data.TvShowData
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

    private fun checkTvShow(tvShowData: TvShowData): Boolean {
        return tvShowData.name != null
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
            tvShowData.name!!,
            tvShowData.image?.original?: tvShowData.image!!.medium,
            tvShowData.image!!.medium!!,
            tvShowData.schedule!!.time!!,
            tvShowData.schedule!!.days!!,
            tvShowData.genres,
            tvShowData.summary!!,
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