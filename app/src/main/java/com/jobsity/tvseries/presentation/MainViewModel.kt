package com.jobsity.tvseries.presentation

import androidx.lifecycle.ViewModel
import com.jobsity.tvseries.domain.repository.SecurityRepository
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.mvvm.SingleLiveEvent

class MainViewModel(private val repository: SecurityRepository): ViewModel() {
    private val goToCheckPin = SingleLiveEvent<String>()
    fun getGoToCheckPin(): SingleLiveEvent<String> {
        return goToCheckPin
    }

    init {
        Coroutines.main {
            if (repository.checkIfHasSecurityImplementation()) {
                goToCheckPin.call()
            }
        }
    }
}