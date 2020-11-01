package com.jobsity.tvseries.domain.repository

import com.jobsity.tvseries.domain.dao.SecurityDao
import com.jobsity.tvseries.util.exception.JobsitySecurityPinException.EmptyPinNumberException
import com.jobsity.tvseries.util.exception.JobsitySecurityPinException.PinNumberFormatException

class SecurityRepository(private val dao: SecurityDao) {

    suspend fun addPinNumber(pinNumber: String?) {
        if (pinNumber.isNullOrEmpty()) {
            throw EmptyPinNumberException("pin number is empty")
        }
        if (pinNumber.length != 4) {
            throw PinNumberFormatException("pin number has no 4 digits")
        }
        dao.setPinNumber(pinNumber)
    }

    suspend fun loadPinNumber(): String? {
        return dao.getPinNumber()
    }

    suspend fun checkIfHasPinNumber(): Boolean {
        val hasPin = dao.getPinNumber()
        return hasPin != null
    }
}