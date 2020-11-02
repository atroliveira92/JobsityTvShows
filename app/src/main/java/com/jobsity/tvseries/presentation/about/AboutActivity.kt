package com.jobsity.tvseries.presentation.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jobsity.tvseries.R
import kotlinx.android.synthetic.main.about_view.*
import kotlinx.android.synthetic.main.about_view.toolbar


class AboutActivity: AppCompatActivity(R.layout.about_view) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = getString(R.string.about)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        setSupportActionBar(toolbar)

        txvGithub.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.arthur_github)))
            startActivity(browserIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}