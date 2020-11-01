package com.jobsity.tvseries.presentation.pin.check

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.jobsity.tvseries.R
import com.jobsity.tvseries.util.extension.hideKeyboard
import com.jobsity.tvseries.util.message.JobsityMessage
import kotlinx.android.synthetic.main.pin_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckPinActivity: AppCompatActivity(R.layout.pin_view) {
    companion object {
        const val CHECK_PIN_REQUEST_CODE = 8234
        fun startActivityForResult(activity: Activity) {
            activity.startActivityForResult(Intent(activity, CheckPinActivity::class.java), CHECK_PIN_REQUEST_CODE)
        }
    }

    private val viewModel: CheckPinViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edtNumber1.addTextChangedListener {
            edtNumber2.requestFocus()
            viewModel.addNumber1(it.toString())
        }

        edtNumber2.addTextChangedListener {
            edtNumber3.requestFocus()
            viewModel.addNumber2(it.toString())
        }

        edtNumber3.addTextChangedListener {
            edtNumber4.requestFocus()
            viewModel.addNumber3(it.toString())
        }

        edtNumber4.addTextChangedListener {
            btnSave.requestFocus()
            hideKeyboard()
            viewModel.addNumber4(it.toString())
        }

        btnSave.text = getString(R.string.done)
        btnSave.setOnClickListener {
            viewModel.didClickDone()
        }

        txvPinNumber.text = String.format(getString(R.string.set_pin_number_to_unlock_), getString(R.string.app_name))

        viewModel.viewState.observe(this, Observer {
            if (it.showError) {
                hideKeyboard()
                JobsityMessage.showErrorMessage(this, it.errorMessage)
            }
            if (it.showSuccess) {
                hideKeyboard()
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}