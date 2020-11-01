package com.jobsity.tvseries.presentation.pin

data class PinViewState (
    val showError: Boolean = false,
    val errorMessage: String = "",
    val showSuccess: Boolean = false,
    val successMessage: String = "",
    val showFingerPrintButton: Boolean = false
)