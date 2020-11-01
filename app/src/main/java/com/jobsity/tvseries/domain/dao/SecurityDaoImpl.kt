package com.jobsity.tvseries.domain.dao

interface SecurityDaoImpl {

    fun setPinNumber(password: String)

    fun getPinNumber(): String?
}