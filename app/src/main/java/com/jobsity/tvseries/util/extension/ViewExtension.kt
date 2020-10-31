package com.jobsity.tvseries.util.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso


fun View.setPortraitHeight(width: Float) {
    this.layoutParams.height = (width * 16/9).toInt()
}

fun ImageView.loadPhoto(url: String) {
    Picasso.get().load(url)
        .into(this)
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}