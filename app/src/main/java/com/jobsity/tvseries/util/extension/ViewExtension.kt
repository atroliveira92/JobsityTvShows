package com.jobsity.tvseries.util.extension

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso


fun View.setPortraitHeight(width: Float) {
    this.layoutParams.height = (width * 16/9).toInt()
}

fun ImageView.loadPhoto(url: String) {
    Picasso.get().load(url)
        .into(this)
}