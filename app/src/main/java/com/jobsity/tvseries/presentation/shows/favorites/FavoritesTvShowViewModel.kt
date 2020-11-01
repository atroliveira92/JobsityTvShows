package com.jobsity.tvseries.presentation.shows.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.presentation.shows.TvShowsViewState
import com.jobsity.tvseries.util.coroutine.Coroutines

class FavoritesTvShowViewModel(private val repository: TvShowRepository,
                               application: Application): JobsityViewModel(application) {

    private val mutableViewState = MutableLiveData<TvShowsViewState>(
        TvShowsViewState(isLoadingVisible = false)
    )
    val viewState: LiveData<TvShowsViewState> get() = mutableViewState

    private val mutableTvShowClick = MutableLiveData<TvShow>()
    val tvShowClicked: LiveData<TvShow> get() = mutableTvShowClick

    private var list = listOf<TvShow>()

    init {
        mutableViewState.value = TvShowsViewState(isLoadingVisible = true)

        Coroutines.main {
            try {
                list = repository.loadFavorites()
                if (list.isEmpty()) {
                    mutableViewState.value = TvShowsViewState(
                        isLoadingVisible = false,
                        isListVisible = false,
                        errorMessage = context.getString(R.string.no_favorites_message),
                        isTryAgainVisible = true,
                        isTryAgainButtonVisible = false
                    )
                } else {
                    mutableViewState.value = list.run {
                        TvShowsViewState(false, this)
                    }
                }

            } catch (e: Exception) {
                mutableViewState.value = TvShowsViewState(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_load_tv_shows),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = false)
            }
        }
    }

    fun search(name: String?) {
        if (name.isNullOrEmpty()) {
            return
        }
        val filterList = list.filter {
            it.name.contains(name)
        }
        mutableViewState.value = viewState.value!!.copy(tvShows = filterList)
    }

    fun clearSearch() {
        mutableViewState.value = viewState.value!!.copy(tvShows = list)
    }

    fun didClickOnTvShow(tvShow: TvShow) {
        mutableTvShowClick.value = tvShow
    }
}