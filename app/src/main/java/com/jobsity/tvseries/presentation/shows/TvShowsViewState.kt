package com.jobsity.tvseries.presentation.shows

import com.jobsity.tvseries.domain.model.TvShow

data class TvShowsViewState (
    val isLoadingVisible: Boolean = true,
    val tvShows: List<TvShow> = emptyList(),
    val errorMessage: String = "",
    val isTryAgainVisible: Boolean = false,
    val isListVisible: Boolean = true,
    val isErrorMessage: Boolean = false,
    val isTryAgainButtonVisible: Boolean = true
)