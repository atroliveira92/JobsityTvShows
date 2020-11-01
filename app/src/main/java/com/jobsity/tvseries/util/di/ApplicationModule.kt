package com.jobsity.tvseries.util.di

import android.app.Application
import androidx.room.Room
import com.jobsity.tvseries.domain.dao.FavoritesTvShowDao
import com.jobsity.tvseries.domain.dao.JobsityDatabase
import com.jobsity.tvseries.domain.dao.SecurityDao
import com.jobsity.tvseries.domain.network.NetWorkConnectionInterceptor
import com.jobsity.tvseries.domain.network.TVShowAPI
import com.jobsity.tvseries.domain.repository.PeopleRepository
import com.jobsity.tvseries.domain.repository.SecurityRepository
import com.jobsity.tvseries.domain.repository.TvShowRepository
import com.jobsity.tvseries.presentation.MainViewModel
import com.jobsity.tvseries.presentation.people.list.PeopleListViewModel
import com.jobsity.tvseries.presentation.pin.check.CheckPinViewModel
import com.jobsity.tvseries.presentation.pin.set.SetPinViewModel
import com.jobsity.tvseries.presentation.shows.favorites.FavoritesTvShowViewModel
import com.jobsity.tvseries.presentation.shows.info.TvShowInfoViewModel
import com.jobsity.tvseries.presentation.shows.list.TvShowListViewModel
import com.jobsity.tvseries.presentation.shows.search.TvShowSearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { NetWorkConnectionInterceptor(get()) }

    factory { TVShowAPI(get()) }
    factory { SecurityDao(get()) }

    factory { TvShowRepository(get(), get()) }
    factory { PeopleRepository(get()) }
    factory { SecurityRepository(get()) }

    viewModel { TvShowListViewModel(get(),get()) }
    viewModel { TvShowSearchViewModel(get(), get()) }
    viewModel { TvShowInfoViewModel(get(), get()) }
    viewModel { PeopleListViewModel(get(),get()) }
    viewModel { FavoritesTvShowViewModel(get(), get()) }
    viewModel { SetPinViewModel(get(),get()) }
    viewModel { CheckPinViewModel(get(),get()) }
    viewModel { MainViewModel(get()) }
}

val databaseModule = module {

    fun provideDatabase(application: Application): JobsityDatabase {
        return Room.databaseBuilder(application, JobsityDatabase::class.java, "jobsity_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideFavoritesShowsDao(database: JobsityDatabase): FavoritesTvShowDao {
        return  database.favoritesTvShowDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideFavoritesShowsDao(get()) }
}