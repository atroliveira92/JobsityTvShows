package com.jobsity.tvseries.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.jobsity.tvseries.JobsityTvShowApplication

open class JobsityViewModel(application: Application): AndroidViewModel(application) {
    var context: Context = getApplication<JobsityTvShowApplication>()
}