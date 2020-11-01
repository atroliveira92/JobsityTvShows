package com.jobsity.tvseries.domain.repository

import com.jobsity.tvseries.domain.dao.SecurityDao
import com.jobsity.tvseries.util.exception.JobsitySecurityPinException.EmptyPinNumberException
import com.jobsity.tvseries.util.exception.JobsitySecurityPinException.PinNumberFormatException

class SecurityRepository(private val dao: SecurityDao) {

    fun addPinNumber(pinNumber: String?) {
        if (pinNumber.isNullOrEmpty()) {
            throw EmptyPinNumberException("pin number is empty")
        }
        if (pinNumber.length != 4) {
            throw PinNumberFormatException("pin number has no 4 digits")
        }
        dao.setFingerPrint(SecurityDao.PIN_NUMBER_MODE)
        dao.setPinNumber(pinNumber)
    }

    fun loadPinNumber(): String? {
        return dao.getPinNumber()
    }

    fun loadTypeAuthentication(): Int {
        val security = dao.getSecurityType()
        return when(security) {
            SecurityDao.FINGERPRINT_MODE -> FINGERPRINT_AUTHENTICATION
            SecurityDao.PIN_NUMBER_MODE -> PIN_NUMBER_AUTHENTICATION
            else -> NO_AUTHENTICATION_PROVIDED
        }
    }

    fun addFingerPrint() {
        dao.setFingerPrint(SecurityDao.FINGERPRINT_MODE)
    }

    companion object {
        const val FINGERPRINT_AUTHENTICATION = 1
        const val PIN_NUMBER_AUTHENTICATION = 2
        const val NO_AUTHENTICATION_PROVIDED = 3
    }

}