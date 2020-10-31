package com.jobsity.tvseries.presentation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jobsity.tvseries.R
import com.jobsity.tvseries.presentation.search.SearchFragment
import com.jobsity.tvseries.presentation.shows.list.TvShowListFragment
import com.jobsity.tvseries.util.extension.hideKeyboard
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {
    private val searchObserver = SearchObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        searchBar.setSpeechMode(false)
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this)
        //restore last queries from disk

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        TvShowListFragment()
                    )
                    .commitNow()
        }
    }

    override fun onButtonClicked(buttonCode: Int) { }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (enabled) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SearchFragment(searchObserver))
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        hideKeyboard()
        searchObserver.notifySearch(text.toString())
    }

}