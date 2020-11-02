package com.jobsity.tvseries.presentation.people

import com.jobsity.tvseries.domain.model.Person


data class PeopleListViewState (
    val isLoadingVisible: Boolean = true,
    val people: List<Person> = emptyList(),
    val errorMessage: String = "",
    val isTryAgainVisible: Boolean = false,
    val isListVisible: Boolean = true,
    val isErrorMessage: Boolean = false,
    val isTryAgainButtonVisible: Boolean = true
)