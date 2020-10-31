package com.jobsity.tvseries.util.di

import com.jobsity.tvseries.domain.network.NetWorkConnectionInterceptor
import com.jobsity.tvseries.domain.network.TVShowAPI
import com.jobsity.tvseries.domain.repository.PeopleRepository
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.presentation.people.list.PeopleListViewModel
import com.jobsity.tvseries.presentation.shows.info.TvShowInfoViewModel
import com.jobsity.tvseries.presentation.shows.list.TvShowListViewModel
import com.jobsity.tvseries.presentation.shows.search.TvShowSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { NetWorkConnectionInterceptor(get()) }

    factory { TVShowAPI(get()) }
    factory { TvShowRepository(get()) }
    factory { PeopleRepository(get()) }

    viewModel { TvShowListViewModel(get(),get()) }
    viewModel { TvShowSearchViewModel(get(), get()) }
    viewModel { TvShowInfoViewModel(get(), get()) }
    viewModel { PeopleListViewModel(get(),get()) }
}