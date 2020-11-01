package com.jobsity.tvseries.presentation.shows.info

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.model.TvShowEpisode
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsityListException

class TvShowInfoViewModel(private val repository: TvShowRepository, application: Application): JobsityViewModel(application) {

    private val mutableViewState = MutableLiveData<TvShowInfoViewState>( TvShowInfoViewState() )
    val viewState: LiveData<TvShowInfoViewState> get() = mutableViewState

    private val mutableEpisodeClick = MutableLiveData<TvShowEpisode>()
    val episodeClick: LiveData<TvShowEpisode> get() = mutableEpisodeClick

    private var seasons = mapOf<Int, List<TvShowEpisode>>()
    private var tvShow: TvShow? = null

    fun init(tvShow: TvShow) {
        this.tvShow = tvShow

        Coroutines.main {
            try {
                tvShow.isFavorite = repository.checkIfFavorite(tvShow.id)

                mutableViewState.value = TvShowInfoViewState(
                    tvShow.name,
                    tvShow.posterHigh?: tvShow.posterMedium,
                    tvShow.isFavorite,
                    !tvShow.rating.isNullOrEmpty(),
                    tvShow.rating?: "",
                    getHourAndDate(tvShow),
                    getGenres(tvShow),
                    tvShow.summary,
                    isShowError = false,
                    enableFavoriteClick = true)

                mutableViewState.value = repository.loadTvShowEpisodes(tvShow).run {
                    val tvShowInfo = viewState.value!!.copy()
                    seasons = this.groupBy{ it.season }
                    tvShowInfo.episodes = seasons[1]?: emptyList()
                    tvShowInfo.seasons = getSeasons(seasons)
                    tvShowInfo
                }
            } catch (e: JobsityListException) {
                mutableViewState.value = when (e) {
                    is JobsityListException.ApiListException -> {
                        viewState.value!!.copy(
                            errorMessage = context.getString(R.string.error_loading_episodes_message),
                            isShowError = true
                        )
                    }
                    is JobsityListException.NoInternetListException -> {
                        viewState.value!!.copy(
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isShowError = true
                        )
                    }
                    is JobsityListException.EmptyDataListException -> {
                        viewState.value!!.copy(
                            errorMessage = context.getString(R.string.error_loading_episodes_message),
                            isShowError = true
                        )
                    }
                }
            }
        }
    }

    private fun getHourAndDate(tvShow: TvShow): String {
        var value = tvShow.time
        if (tvShow.days.isNotEmpty()) {
            var days = ""
            value += " ● "
            if (tvShow.days.size == 1) {
                days += tvShow.days[0]
            } else {
                for (pos in tvShow.days.indices) {
                    days += if (pos == tvShow.days.size - 1) {
                        "and ${tvShow.days[pos]}"
                    } else {
                        ", ${tvShow.days[pos]} "
                    }
                }
            }
            days = days.removePrefix(", ")
            if (days.isNotEmpty()) {
                value += days
            }
        }
        return value
    }

    private fun getGenres(tvShow: TvShow): String {
        var value = ""
        if (tvShow.genre != null) {
            for (pos in tvShow.genre.indices) {
                value += if (pos == tvShow.genre.size - 1) {
                    tvShow.genre[pos]
                } else {
                    "${tvShow.genre[pos]} ● "
                }
            }
        }
        return value
    }

    private fun getSeasons(map: Map<Int, List<TvShowEpisode>>): List<String> {
        val hashMap = HashMap<Int, List<TvShowEpisode>>(map)
        val list = mutableListOf<String>()
        for (n in hashMap.keys) {
            list.add(String.format(context.getString(R.string.season_), n))
        }

        return list
    }

    fun didClickOnSeason(position: Int) {
        val hashMap = HashMap<Int, List<TvShowEpisode>>(seasons)
        val list = ArrayList<Int>(hashMap.keys)
        if (position > 0 && position < list.size) {
            val key = list[position]
            mutableViewState.value = mutableViewState.value!!.copy(episodes = hashMap[key]!!)
        }
    }

    fun didClickOnEpisode(tvShowEpisode: TvShowEpisode) {
        mutableEpisodeClick.value = tvShowEpisode
    }

    fun didClickOnFavorite() {
        tvShow?.let {
            Coroutines.main {
                repository.changeFavoriteTvShowStatus(it)
                mutableViewState.value = viewState.value!!.copy(isFavorite = it.isFavorite)
            }
        }
    }

    fun getCurrentTvShow(): TvShow? {
        return tvShow
    }
}

data class TvShowInfoViewState(
    val name: String = "",
    val posterUrl: String = "",
    var isFavorite: Boolean = false,
    val isShowRating: Boolean = false,
    val rating: String = "",
    val hourAndDays: String = "",
    val genres: String = "",
    val summary: String = "",
    var episodes: List<TvShowEpisode> = emptyList(),
    var seasons: List<String> = emptyList(),
    val isShowError: Boolean = false,
    val errorMessage: String = "",
    var enableFavoriteClick: Boolean = false
)