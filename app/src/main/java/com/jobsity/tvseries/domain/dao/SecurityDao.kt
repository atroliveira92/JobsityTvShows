package com.jobsity.tvseries.domain.dao

import android.app.Application
import android.content.Context

class SecurityDao(private val application: Application): SecurityDaoImpl {
    companion object {
        private const val PREFERENCES = "jobsity_preference"
        private const val PIN_PASSWORD = "pin_password"
        private const val FINGERPRINT = "security_mode"
    }

    override fun setFingerPrint(mode: Int) {
        val context = application.applicationContext
        val settings = context.getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = settings.edit()
        editor.putInt(PIN_PASSWORD, mode)
        editor.apply()
    }

    override fun getSecurityType(): Int? {
        val context = application.applicationContext
        val settings = context.getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        )
        val mode = settings.getInt(PIN_PASSWORD, -1)
        return if (mode == -1) {
            null
        } else {
            mode
        }
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