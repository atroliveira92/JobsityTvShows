package com.jobsity.tvseries.domain.dao

interface SecurityDaoImpl {
    fun setFingerPrint(mode: Int)

    fun getSecurityType(): Int?

    fun setPinNumber(password: String)

    fun getPinNumber(): String?
}