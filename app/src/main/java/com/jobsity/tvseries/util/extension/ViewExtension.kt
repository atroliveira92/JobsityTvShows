package com.jobsity.tvseries.util.extension

import android.app.Activity
import android.content.Context
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jobsity.tvseries.R
import com.jobsity.tvseries.util.image.CircleTransform
import com.squareup.picasso.Picasso


fun View.setPortraitHeight(width: Float) {
    this.layoutParams.height = (width * 16/9).toInt()
}

fun ImageView.loadPhoto(url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url)
            .into(this)
    }
}

fun ImageView.loadRoundPhoto(url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url)
            .transform(CircleTransform())
            .into(this)
    }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextView.htmlText(text: String?) {
    if (!text.isNullOrEmpty()) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY)
        } else {
            this.text = Html.fromHtml(text)
        }

        this.includeFontPadding = false
    }
}

fun ImageView.favorite(isFavorite: Boolean) {
    val drawableId = if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
    this.setImageDrawable(ContextCompat.getDrawable(this.context, drawableId))
}