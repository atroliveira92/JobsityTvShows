package com.jobsity.tvseries.presentation.pin.set

import android.app.Application
import androidx.biometric.BiometricManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.repository.SecurityRepository
import com.jobsity.tvseries.presentation.JobsityViewModel
import com.jobsity.tvseries.presentation.pin.PinViewState
import com.jobsity.tvseries.util.coroutine.Coroutines
import com.jobsity.tvseries.util.exception.JobsitySecurityPinException
import java.util.concurrent.Executor

class SetPinViewModel(private val repository: SecurityRepository, application: Application): JobsityViewModel(application) {

    private var number1: String? = null
    private var number2: String? = null
    private var number3: String? = null
    private var number4: String? = null

    private val mutableViewState = MutableLiveData<PinViewState> (
        PinViewState()
    )
    val viewState: LiveData<PinViewState> get() = mutableViewState

    init {
        val biometricManager = BiometricManager.from(context)
        val allowFingerprint = when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            else -> false
        }

        mutableViewState.value = PinViewState(
            showFingerPrintButton =  allowFingerprint
        )
    }

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

    fun didClickOnSave() {
        if (number1.isNullOrEmpty() || number2.isNullOrEmpty() || number3.isNullOrEmpty() || number4.isNullOrEmpty()) {
            mutableViewState.value =
                PinViewState(
                    showError = true,
                    errorMessage = context.getString(R.string.fields_cannot_be_empty)
                )
            return
        }
        try {
            repository.addPinNumber(number1 + number2 + number3 + number4)
            mutableViewState.value =
                PinViewState(
                    showSuccess = true,
                    successMessage = context.getString(R.string.pin_number_set_message)
                )
        } catch (e: JobsitySecurityPinException) {
            e.printStackTrace()
            mutableViewState.value = when(e) {
                is JobsitySecurityPinException.PinNumberFormatException -> {
                    PinViewState(
                        showError = true,
                        errorMessage = context.getString(R.string.pin_must_have_all_4_digits)
                    )
                }
                is JobsitySecurityPinException.EmptyPinNumberException -> {
                    PinViewState(
                        showError = true,
                        errorMessage = context.getString(R.string.fields_cannot_be_empty)
                    )
                }
            }
        }

    }

    fun didClickOnFingerprint() {
        repository.addFingerPrint()
    }
}