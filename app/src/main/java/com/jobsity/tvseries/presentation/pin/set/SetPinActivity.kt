package com.jobsity.tvseries.presentation.pin.set

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.jobsity.tvseries.R
import com.jobsity.tvseries.util.extension.hideKeyboard
import com.jobsity.tvseries.util.message.JobsityMessage
import kotlinx.android.synthetic.main.pin_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetPinActivity: AppCompatActivity(R.layout.pin_view) {

    private val viewModel: SetPinViewModel by viewModel()

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

        btnSave.setOnClickListener {
            viewModel.didClickOnSave()
        }

        viewModel.viewState.observe(this, Observer {
            if (it.showError) {
                hideKeyboard()
                JobsityMessage.showErrorMessage(this, it.errorMessage)
            }
            if (it.showSuccess) {
                hideKeyboard()
                JobsityMessage.showSuccessMessage(this, it.successMessage)
            }

            btnSetFingerprint.isVisible = it.showFingerPrintButton
            txvOr.isVisible = it.showFingerPrintButton
        })
    }
}