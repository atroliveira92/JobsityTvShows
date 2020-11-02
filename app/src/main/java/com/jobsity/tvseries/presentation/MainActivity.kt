package com.jobsity.tvseries.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.jobsity.tvseries.R
import com.jobsity.tvseries.presentation.about.AboutActivity
import com.jobsity.tvseries.presentation.pin.check.CheckPinActivity
import com.jobsity.tvseries.presentation.pin.set.SetPinActivity
import com.jobsity.tvseries.presentation.search.SearchFragment
import com.jobsity.tvseries.presentation.shows.favorites.FavoritesTvShowsActivity
import com.jobsity.tvseries.presentation.shows.list.TvShowListFragment
import com.jobsity.tvseries.util.extension.hideKeyboard
import kotlinx.android.synthetic.main.main_view.*
import kotlinx.android.synthetic.main.side_menu_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val searchObserver = SearchObserver()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)

        toolbar.title = getString(R.string.app_name)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        TvShowListFragment()
                    )
                    .commitNow()
        }

        txvFavorites.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoritesTvShowsActivity::class.java))
        }

        txvSetPin.setOnClickListener {
            startActivity(Intent(this@MainActivity, SetPinActivity::class.java))
        }

        txvAbout.setOnClickListener {
            startActivity(Intent(this@MainActivity, AboutActivity::class.java))
        }

        viewModel.getGoToCheckPin().observe(this, Observer {
            CheckPinActivity.startActivityForResult(this)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu!!.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                hideKeyboard()
                searchObserver.notifySearch(s.toString())

                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                return false
            }
        })

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment(searchObserver))
                    .addToBackStack(null)
                    .commit()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                supportFragmentManager.popBackStack()
                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId != R.id.action_search) {
            dlMain.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CheckPinActivity.CHECK_PIN_REQUEST_CODE
            && resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }
}