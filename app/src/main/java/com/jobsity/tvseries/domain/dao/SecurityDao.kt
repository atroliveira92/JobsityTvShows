package com.jobsity.tvseries.domain.dao

import android.app.Application
import android.content.Context

class SecurityDao(private val application: Application): SecurityDaoImpl {
    companion object {
        private const val PREFERENCES = "jobsity_preference"
        private const val PIN_PASSWORD = "pin_password"
    }
    override fun setPinNumber(password: String) {
        val context = application.applicationContext
        val settings = context.getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = settings.edit()
        editor.putString(PIN_PASSWORD, password)
        editor.apply()
    }

    override fun getPinNumber(): String? {
        val context = application.applicationContext
        val settings = context.getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        )
        return settings.getString(PIN_PASSWORD, null)
    }
}