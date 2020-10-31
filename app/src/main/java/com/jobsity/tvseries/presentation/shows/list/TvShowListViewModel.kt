package com.jobsity.tvseries.presentation.shows.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.presentation.shows.TvShowsViewState
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsityException
import com.jobsity.tvseries.util.exception.JobsityException.*

class TvShowListViewModel(var repository: TvShowRepository, application: Application): JobsityViewModel(application) {
    private val mutableViewState = MutableLiveData<TvShowsViewState>(
        TvShowsViewState()
    )
    val viewState: LiveData<TvShowsViewState> get() = mutableViewState

    private val mutableTvShowClick = MutableLiveData<TvShow>()
    val tvShowClicked: LiveData<TvShow> get() = mutableTvShowClick

    init {
       loadVideos()
    }

    fun paginate() {
        Coroutines.main {
            try {
                mutableViewState.value = repository.loadMoreTvShows().run {
                    TvShowsViewState(
                        false,
                        mutableViewState.value!!.tvShows + this,
                        isListVisible = true
                    )
                }
            } catch (e: JobsityException) {
                mutableViewState.value = when (e) {
                    is ApiException ->  {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isErrorMessage = true,
                            errorMessage = context.getString(R.string.error_load_more_tv_shows_message),
                            isTryAgainVisible = false)
                    }
                    is NoInternetException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isErrorMessage = true,
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isTryAgainVisible = false)
                    }
                    is EmptyDataException -> {
                        viewState.value!!.copy()
                    }
                }
            }
        }

    }

    private fun loadVideos() {
        Coroutines.main {
            try {
                mutableViewState.value = repository.loadTvShows().run {
                    TvShowsViewState(false,this)
                }
            } catch (e: JobsityException) {
                mutableViewState.value = when (e) {
                    is ApiException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_load_tv_shows),
                            isTryAgainVisible = true)
                    }
                    is NoInternetException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isTryAgainVisible = true)
                    }
                    is EmptyDataException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_load_tv_shows),
                            isTryAgainVisible = true)
                    }
                }
            }  catch (e: Throwable) {
                e.printStackTrace()
                viewState.value!!.copy(
                    isLoadingVisible = false,
                    isListVisible = false,
                    errorMessage = context.getString(R.string.generic_error_message),
                    isTryAgainVisible = true,
                    isTryAgainButtonVisible = false)
            }
        }
    }

    fun didClickOnTryAgain() {
        loadVideos()
    }

    fun didClickOnShow(tvShow: TvShow) {
        mutableTvShowClick.value = tvShow
    }
}