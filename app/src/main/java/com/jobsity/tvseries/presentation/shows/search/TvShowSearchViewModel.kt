package com.jobsity.tvseries.presentation.shows.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.presentation.shows.TvShowsViewState
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsityListException
import com.jobsity.tvseries.util.exception.JobsityListException.*

class TvShowSearchViewModel(private val repository: TvShowRepository, application: Application): JobsityViewModel(application) {
    private val mutableViewState = MutableLiveData<TvShowsViewState>(
        TvShowsViewState(isLoadingVisible = false)
    )
    val viewState: LiveData<TvShowsViewState> get() = mutableViewState
    private var searchTerm: String? = null

    private val mutableTvShowClick = MutableLiveData<TvShow>()
    val tvShowClicked: LiveData<TvShow> get() = mutableTvShowClick

    fun search(searchTerm: String?) {
        if (searchTerm.isNullOrEmpty()) {
            return
        }
        mutableViewState.value = TvShowsViewState(isLoadingVisible = true)

        Coroutines.main {
            try {
                mutableViewState.value = repository.searchTvShow(searchTerm).run {
                    TvShowsViewState(false, this)
                }
            } catch (e: JobsityListException) {
                mutableViewState.value = when (e) {
                    is ApiListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_load_tv_shows),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = true)
                    }
                    is NoInternetListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = true)
                    }
                    is EmptyDataListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.no_show_found_message),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = false)
                    }
                }
            } catch (e: Throwable) {
                mutableViewState.value = viewState.value!!.copy(
                    isLoadingVisible = false,
                    isListVisible = false,
                    errorMessage = context.getString(R.string.generic_error_message),
                    isTryAgainVisible = true,
                    isTryAgainButtonVisible = false)
            }
        }

    }

    fun didClickOnTryAgain() {
        search(searchTerm)
    }

    fun didClickOnTvShow(tvShow: TvShow) {
        mutableTvShowClick.value = tvShow
    }
}