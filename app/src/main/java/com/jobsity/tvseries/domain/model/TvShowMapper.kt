package com.jobsity.tvseries.domain.model

import com.jobsity.tvseries.data.TvShowData

object TvShowMapper {
    fun map(tvShowsData: List<TvShowData>): List<TvShow> {
        return tvShowsData.filter {
            it.name != null
            && it.image != null
            && it.schedule != null
            && it.schedule!!.days != null
            && it.schedule!!.time != null
            && it.summary != null
            && it.premiered != null

        }.map {
            val premierDate = getYearFromPremieredData(it.premiered)
            TvShow(
                it.name!!,
                it.image?.original?: it.image!!.medium,
                it.image!!.medium!!,
                it.schedule!!.time!!,
                it.schedule!!.days!!,
                it.genres,
                it.summary!!,
                it.rating?.average,
                premierDate
            )
        }
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