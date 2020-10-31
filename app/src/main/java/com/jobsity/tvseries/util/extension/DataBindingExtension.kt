package com.jobsity.tvseries.util.extension

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageResource")
fun loadDrawableImage(view: ImageView, drawableInt: Int) {
    view.setImageResource(drawableInt)
}

@BindingAdapter("loadPhoto")
fun loadPhoto(view: ImageView, url: String) {
    Picasso.get().load(url)
        .into(view)
}

@BindingAdapter("textByDate")
fun textByDate(view: TextView, date: String?) {
    date?.let {
        if (it.contains("-")) {
            val slipt = it.split("-")
            view.text = slipt[0]
        }
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}