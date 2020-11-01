package com.jobsity.tvseries.domain.repository

import com.jobsity.tvseries.data.FavoritesTvShowEntity
import com.jobsity.tvseries.domain.dao.FavoritesTvShowDao
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.model.TvShowEpisode
import com.jobsity.tvseries.domain.model.TvShowMapper
import com.jobsity.tvseries.domain.network.SafeApiRequest
import com.jobsity.tvseries.domain.network.TVShowAPI
import com.jobsity.tvseries.util.exception.JobsityListException.EmptyDataListException

class TvShowRepository(private val api: TVShowAPI, private val dao: FavoritesTvShowDao): SafeApiRequest() {
    private var page = 0
    private var ended = false

    suspend fun loadTvShows(): List<TvShow> {
        page = 1
        ended = false
        val list =  loadTvShows(page)
        if (list.isEmpty()) {
            throw EmptyDataListException("tv show list is empty")
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
            throw EmptyDataListException("list is empty")
        }

        return list
    }

    suspend fun loadTvShowEpisodes(tvShow: TvShow): List<TvShowEpisode> {
        val listData = apiRequest { api.loadEpisodes(tvShow.id.toString()) }
        val list = TvShowMapper.mapEpisodesData(listData, tvShow)

        if (list.isEmpty()) {
            throw EmptyDataListException("list is empty")
        }

        return list
    }

    @Synchronized suspend fun changeFavoriteTvShowStatus(tvShow: TvShow) {
        val tvShowEntity = FavoritesTvShowEntity(
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

        if (tvShow.isFavorite) {
            dao.delete(tvShowEntity)
        } else {
            dao.insert(tvShowEntity)
        }

        tvShow.isFavorite = !tvShow.isFavorite
    }

    suspend fun checkIfFavorite(id: Int): Boolean {
        val favorite = dao.loadFromId(id)

        return favorite != null
    }

    suspend fun loadFavorites(): List<TvShow> {
        val list = dao.loadAll()
        return TvShowMapper.mapFavoritesToTvShoList(list)
    }

    private suspend fun loadTvShows(page: Int): List<TvShow> {
        val listData =  apiRequest { api.getShows(page) }
        return TvShowMapper.mapTvShowData(listData)
    }

}