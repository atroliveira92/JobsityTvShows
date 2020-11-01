package com.jobsity.tvseries.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import com.jobsity.tvseries.R
import com.jobsity.tvseries.presentation.search.SearchFragment
import com.jobsity.tvseries.presentation.shows.favorites.FavoritesTvShowsActivity
import com.jobsity.tvseries.presentation.shows.list.TvShowListFragment
import com.jobsity.tvseries.util.extension.hideKeyboard
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.side_menu_view.*


class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {
    private val searchObserver = SearchObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        searchBar.setSpeechMode(false)
        searchBar.setOnSearchActionListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        TvShowListFragment()
                    )
                    .commitNow()
        }

        imgvMenu.setOnClickListener {
            dlMain.openDrawer(GravityCompat.START)
        }

        txvFavorites.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoritesTvShowsActivity::class.java))
        }
    }

    override fun onButtonClicked(buttonCode: Int) { }

    override fun onSearchStateChanged(enabled: Boolean) {
        imgvMenu.isVisible = !enabled
        txvTitle.isVisible = !enabled

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