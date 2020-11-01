package com.jobsity.tvseries.presentation

import android.app.Activity
import android.app.Activity.*
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.jobsity.tvseries.R
import com.jobsity.tvseries.presentation.pin.check.CheckPinActivity
import com.jobsity.tvseries.presentation.pin.set.SetPinActivity
import com.jobsity.tvseries.presentation.search.SearchFragment
import com.jobsity.tvseries.presentation.shows.favorites.FavoritesTvShowsActivity
import com.jobsity.tvseries.presentation.shows.list.TvShowListFragment
import com.jobsity.tvseries.util.extension.hideKeyboard
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.side_menu_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {
    private val searchObserver = SearchObserver()
    private val viewModel: MainViewModel by viewModel()

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

        txvSetPin.setOnClickListener {
            startActivity(Intent(this@MainActivity, SetPinActivity::class.java))
        }

        viewModel.getGoToCheckPin().observe(this, Observer {
            CheckPinActivity.startActivityForResult(this)
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CheckPinActivity.CHECK_PIN_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }
}