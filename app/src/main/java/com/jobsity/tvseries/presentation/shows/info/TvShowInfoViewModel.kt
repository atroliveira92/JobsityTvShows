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
import com.jobsity.tvseries.util.exception.JobsityException

class TvShowInfoViewModel(private val repository: TvShowRepository, application: Application): JobsityViewModel(application) {

    private val mutableViewState = MutableLiveData<TvShowInfoViewState>( TvShowInfoViewState() )
    val viewState: LiveData<TvShowInfoViewState> get() = mutableViewState

    private var seasons = mapOf<Int, List<TvShowEpisode>>()

    fun init(tvShow: TvShow) {
        mutableViewState.value = TvShowInfoViewState(
            tvShow.name,
            tvShow.posterHigh?: tvShow.posterMedium,
            false,
            !tvShow.rating.isNullOrEmpty(),
            tvShow.rating?: "",
            getHourAndDate(tvShow),
            getGenres(tvShow),
            tvShow.summary,
            isShowError = false)

        Coroutines.main {
            try {
                mutableViewState.value = repository.loadTvShowEpisodes(tvShow).run {
                    val tvShowInfo = viewState.value!!.copy()
                    seasons = this.groupBy{ it.season }
                    tvShowInfo.episodes = seasons[1]?: emptyList()
                    tvShowInfo.seasons = getSeasons(seasons)
                    tvShowInfo
                }
            } catch (e: JobsityException) {
                mutableViewState.value = when (e) {
                    is JobsityException.ApiException -> {
                        viewState.value!!.copy(
                            errorMessage = context.getString(R.string.error_loading_episodes_message),
                            isShowError = true
                        )
                    }
                    is JobsityException.NoInternetException -> {
                        viewState.value!!.copy(
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isShowError = true
                        )
                    }
                    is JobsityException.EmptyDataException -> {
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
            value += " ● "
            if (tvShow.days.size == 1) {
                value += tvShow.days[0]
            } else {
                for (pos in tvShow.days.indices) {
                    value += if (pos == tvShow.days.size - 1) {
                        "and ${tvShow.days[pos]}"
                    } else {
                        "${tvShow.days[pos]}, "
                    }
                }
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
}

data class TvShowInfoViewState(
    val name: String = "",
    val posterUrl: String = "",
    val isFavorite: Boolean = false,
    val isShowRating: Boolean = false,
    val rating: String = "",
    val hourAndDays: String = "",
    val genres: String = "",
    val summary: String = "",
    var episodes: List<TvShowEpisode> = emptyList(),
    var seasons: List<String> = emptyList(),
    val isShowError: Boolean = false,
    val errorMessage: String = ""
)