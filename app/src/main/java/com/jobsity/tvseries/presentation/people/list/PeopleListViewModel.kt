package com.jobsity.tvseries.presentation.people.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.repository.PeopleRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsityListException

class PeopleListViewModel(private val repository: PeopleRepository, application: Application): JobsityViewModel(application) {

    private val mutableViewState = MutableLiveData<PeopleListViewState>(
        PeopleListViewState(
            isLoadingVisible = false
        )
    )
    val viewState: LiveData<PeopleListViewState> get() = mutableViewState
    private var searchTerm: String? = null

    fun search(searchTerm: String?) {
        if (searchTerm.isNullOrEmpty()) {
            return
        }
        mutableViewState.value =
            PeopleListViewState(
                isLoadingVisible = true
            )

        Coroutines.main {
            try {
                mutableViewState.value = repository.searchPeople(searchTerm).run {
                    PeopleListViewState(
                        false,
                        this
                    )
                }
            } catch (e: JobsityListException) {
                mutableViewState.value = when (e) {
                    is JobsityListException.ApiListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.error_people_search_message),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = true
                        )
                    }
                    is JobsityListException.NoInternetListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.internet_connection_error_message),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = true
                        )
                    }
                    is JobsityListException.EmptyDataListException -> {
                        viewState.value!!.copy(
                            isLoadingVisible = false,
                            isListVisible = false,
                            errorMessage = context.getString(R.string.people_not_found_message),
                            isTryAgainVisible = true,
                            isTryAgainButtonVisible = false
                        )
                    }
                }
            }
        }
    }

    fun didClickOnTryAgain() {
        search(searchTerm)
    }
}