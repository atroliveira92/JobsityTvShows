package com.jobsity.tvseries.domain.repository

import com.jobsity.tvseries.data.TvShowSearchData
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.model.TvShowEpisode
import com.jobsity.tvseries.domain.model.TvShowMapper
import com.jobsity.tvseries.domain.network.SafeApiRequest
import com.jobsity.tvseries.domain.network.TVShowAPI
import com.jobsity.tvseries.util.exception.JobsityException
import com.jobsity.tvseries.util.exception.JobsityException.EmptyDataException

class TvShowRepository(private val api: TVShowAPI): SafeApiRequest() {
    private var page = 0
    private var ended = false

    suspend fun loadTvShows(): List<TvShow> {
        page = 1
        ended = false
        val list =  loadTvShows(page)
        if (list.isEmpty()) {
            throw EmptyDataException("tv show list is empty")
        }

        return list
    }

    suspend fun loadMoreTvShows(): List<TvShow> {
        if (ended) {
            return emptyList()
        }
        page++
        val list = loadTvShows(page)
        if (list.isEmpty()) {
            ended = true
        }

        return list
    }

    suspend fun searchTvShow(search: String): List<TvShow> {
        val listData = apiRequest { api.searchShows(search) }
        val list = TvShowMapper.mapTvShowSearchData(listData)

        if (list.isEmpty()) {
            throw EmptyDataException("list is empty")
        }

        return list
    }

    suspend fun loadTvShowEpisodes(tvShow: TvShow): List<TvShowEpisode> {
        val listData = apiRequest { api.loadEpisodes(tvShow.id.toString()) }
        val list = TvShowMapper.mapEpisodesData(listData, tvShow)

        if (list.isEmpty()) {
            throw EmptyDataException("list is empty")
        }

        return list
    }

    private suspend fun loadTvShows(page: Int): List<TvShow> {
        val listData =  apiRequest { api.getShows(page) }
        return TvShowMapper.mapTvShowData(listData)
    }

}