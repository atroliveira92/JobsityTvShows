package com.jobsity.tvseries.presentation.people.info

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.Person
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.repository.PeopleRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsityListException

class PeopleInfoViewModel(private val repository: PeopleRepository, application: Application): JobsityViewModel(application)  {
    private val mutableViewState = MutableLiveData<PeopleInfoViewState>(
        PeopleInfoViewState()
    )
    val viewState: LiveData<PeopleInfoViewState> get() = mutableViewState

    private val mutableTvShowClick = MutableLiveData<TvShow>()
    val tvShowClicked: LiveData<TvShow> get() = mutableTvShowClick

    var person: Person? = null

    fun init(person: Person) {
        this.person = person

        mutableViewState.value = PeopleInfoViewState(
            name = person.name,
            imageUrl = person.originalImage,
            isLoadingVisible = true
        )

        loadShows(person.id)
    }

    private fun loadShows(id: Int) {
        mutableViewState.value = viewState.value!!.copy(isLoadingVisible = true, showError = false)
        Coroutines.main {
            try {
                repository.loadPersonShows(id).run {
                    mutableViewState.value = viewState.value!!.copy(
                        isLoadingVisible = false,
                        tvShows = this,
                        showTryAgainButton = false,
                        showError = false
                    )
                }
            } catch (e: JobsityListException) {
                mutableViewState.value = when(e) {
                    is JobsityListException.ApiListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            showError = true,
                            errorMessage = context.getString(R.string.error_load_tv_shows),
                            showTryAgainButton = true)
                    }
                    is JobsityListException.NoInternetListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            showError = true,
                            errorMessage = context.getString(R.string.no_internet_connection_message),
                            showTryAgainButton = true)
                    }
                    is JobsityListException.EmptyDataListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            showError = true,
                            errorMessage = context.getString(R.string.no_show_was_found),
                            showTryAgainButton = false)
                    }
                }
            } catch (e: Throwable) {
                mutableViewState.value = viewState.value!!.copy(
                    isLoadingVisible = false,
                    showError = true,
                    errorMessage = context.getString(R.string.generic_error_message),
                    showTryAgainButton = false)
            }
        }
    }

    fun didClickOnShow(tvShow: TvShow) {
        mutableTvShowClick.value = tvShow
    }

    fun didClickOnTryAgain() {
        person?.let { loadShows(it.id) }
    }
}

data class PeopleInfoViewState(
    val name: String = "",
    val imageUrl: String = "",
    val tvShows: List<TvShow> = emptyList(),
    val errorMessage: String = "",
    val showError: Boolean = false,
    val isLoadingVisible: Boolean = true,
    val showTryAgainButton: Boolean = false
)