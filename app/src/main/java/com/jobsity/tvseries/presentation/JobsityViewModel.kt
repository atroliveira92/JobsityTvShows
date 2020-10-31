package com.jobsity.tvseries.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job

abstract class JobsityViewModel: ViewModel(), CoroutineScope {

    override val coroutineContext = Main

    val jobs = ArrayList<Job>()

    fun clearJobs() {
        jobs.forEach { if(!it.isCancelled) it.cancel() }
    }

    abstract fun removeObservers(lifecycleOwner: LifecycleOwner)
}