package com.jobsity.tvseries.util.message

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.jobsity.tvseries.R

object JobsityMessage {

    fun showErrorMessage(context: Context?, message: String) {
        showSnackBar(context, message, R.color.text_controller, R.color.error_message_background)
    }

    fun showSuccessMessage(context: Context?, message: String) {
        showSnackBar(context, message, R.color.text_controller, R.color.success_message_background)
    }

    private fun showSnackBar(context: Context?, errorMessage: String, textColor: Int, backgroundColor: Int) {
        if (context is Activity) {
            val v = getViewSnackBar(context)
            v?.let {
                val snackbar = Snackbar.make(
                    v,
                    errorMessage,
                    Snackbar.LENGTH_LONG
                )

                val textView =
                    snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(ContextCompat.getColor(snackbar.context, textColor))
                snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        snackbar.context,
                        backgroundColor
                    )
                )

                snackbar.show()
            }
        }
    }

    private  fun getViewSnackBar(activity: Activity): View? {
        var view: View? = null
        try {
            view = activity.findViewById(android.R.id.content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }
}