package com.jobsity.tvseries.presentation.pin.check

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.repository.SecurityRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.presentation.pin.PinViewState
import com.jobsity.tvseries.util.coroutine.Coroutines

class CheckPinViewModel(private val repository: SecurityRepository, application: Application): JobsityViewModel(application) {
    private var number1: String? = null
    private var number2: String? = null
    private var number3: String? = null
    private var number4: String? = null

    private val mutableViewState = MutableLiveData<PinViewState> (
        PinViewState()
    )
    val viewState: LiveData<PinViewState> get() = mutableViewState

    fun addNumber1(number1: String) {
        this.number1 = number1
    }

    fun addNumber2(number2: String) {
        this.number2 = number2
    }

    fun addNumber3(number3: String) {
        this.number3 = number3
    }

    fun addNumber4(number4: String) {
        this.number4 = number4
    }

    fun didClickDone() {
        if (number1.isNullOrEmpty() || number2.isNullOrEmpty() || number3.isNullOrEmpty() || number4.isNullOrEmpty()) {
            mutableViewState.value =
                PinViewState(
                    showError = true,
                    errorMessage = context.getString(R.string.fields_cannot_be_empty)
                )
        } else {
            Coroutines.main {
                val currentPin = number1 + number2 + number3 + number4
                val securityPin = repository.loadPinNumber()
                if (currentPin == securityPin) {
                    mutableViewState.value = PinViewState(
                        showSuccess = true
                    )
                } else {
                    mutableViewState.value =
                        PinViewState(
                            showSuccess = false,
                            showError = true,
                            errorMessage = context.getString(R.string.wrong_pin_number)
                        )
                }
            }
        }
    }
}