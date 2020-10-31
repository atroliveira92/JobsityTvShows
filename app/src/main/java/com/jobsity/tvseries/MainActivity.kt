package com.jobsity.tvseries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jobsity.tvseries.presentation.shows.TvShowListFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        toolbar.title = "Jobsity Tv Shows"

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, TvShowListFragment())
                    .commitNow()
        }
    }
}