package com.jobsity.tvseries.presentation.shows

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.JobsityTvShowApplication
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsityException
import com.jobsity.tvseries.util.exception.JobsityException.*

class TvShowListViewModel(var repository: TvShowRepository, application: Application): AndroidViewModel(application) {
    private val mutableViewState = MutableLiveData<TvShowsViewState>(TvShowsViewState())
    val viewState: LiveData<TvShowsViewState> get() = mutableViewState
    private val context = getApplication<JobsityTvShowApplication>()

    init {
       loadVideos()
    }

    fun paginate() {
        Coroutines.main {
            try {
                mutableViewState.value = repository.loadMoreTvShows().run {
                    TvShowsViewState(false, mutableViewState.value!!.tvShows + this, isListVisible = true)
                }
            } catch (e: JobsityException) {
                mutableViewState.value = when (e) {
                    is ApiException ->  {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isErrorMessage = true,
                            errorMessage = context.getString(R.string.error_load_tv_shows),
                            isTryAgain = false)
                    }
                    is NoInternetException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isErrorMessage = true,
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isTryAgain = false)
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
                    TvShowsViewState(false, this, isErrorMessage = true, errorMessage = "Mostra snackbar")
                }
            } catch (e: JobsityException) {
                mutableViewState.value = when (e) {
                    is ApiException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_load_more_tv_shows_message),
                            isTryAgain = true)
                    }
                    is NoInternetException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isTryAgain = true)
                    }
                    is EmptyDataException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_load_more_tv_shows_message),
                            isTryAgain = true)
                    }
                }
            }  catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun didClickOnTryAgain() {
        loadVideos()
    }
}

data class TvShowsViewState (
    val isLoadingVisible: Boolean = true,
    val tvShows: List<TvShow> = emptyList(),
    val errorMessage: String = "",
    val isTryAgain: Boolean = false,
    val isListVisible: Boolean = true,
    val isErrorMessage: Boolean = false
)