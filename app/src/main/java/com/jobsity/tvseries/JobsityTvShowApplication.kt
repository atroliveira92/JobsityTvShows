package com.jobsity.tvseries

import android.app.Application
import com.jobsity.tvseries.util.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JobsityTvShowApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JobsityTvShowApplication)
            modules(listOf(applicationModule))
        }
    }

}