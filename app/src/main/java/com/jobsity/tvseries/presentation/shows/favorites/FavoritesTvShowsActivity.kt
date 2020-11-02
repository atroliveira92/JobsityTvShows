package com.jobsity.tvseries.presentation.shows.favorites

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.presentation.shows.TvShowAdapter
import com.jobsity.tvseries.presentation.shows.TvShowAdapter.ITvShowAdapter
import com.jobsity.tvseries.presentation.shows.info.TvShowInfoActivity
import com.jobsity.tvseries.util.extension.hideKeyboard
import com.jobsity.tvseries.util.message.JobsityMessage
import kotlinx.android.synthetic.main.error_try_again_view.*
import kotlinx.android.synthetic.main.favorites_view.toolbar
import kotlinx.android.synthetic.main.list_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTvShowsActivity: AppCompatActivity(R.layout.favorites_view), ITvShowAdapter {

    private val viewModel: FavoritesTvShowViewModel by viewModel()
    private val adapter = TvShowAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = getString(R.string.favorites)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        setSupportActionBar(toolbar)

        rvShows.also {
            it.layoutManager = GridLayoutManager(this, 3)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        imgvError.setImageDrawable(ContextCompat.getDrawable(imgvError.context, R.drawable.ic_baseline_favorite_border_24))

        viewModel.viewState.observe(this, Observer { viewState ->
            pgShows.isVisible = viewState.isLoadingVisible
            rvShows.isVisible = viewState.isListVisible
            vError.isVisible = viewState.isTryAgainVisible
            txvErrorMessage.text = viewState.errorMessage
            btnTryAgain.isVisible = viewState.isTryAgainButtonVisible

            if (viewState.isErrorMessage) {
                JobsityMessage.showErrorMessage(this, viewState.errorMessage)
            }

            rvShows.post { adapter.shows = viewState.tvShows }
        })

        viewModel.tvShowClicked.observe(this, Observer {
            TvShowInfoActivity.startActivityForResult(this, it)
        })
    }

    override fun onReachLastItem() {}

    override fun onClick(tvShow: TvShow) {
        viewModel.didClickOnTvShow(tvShow)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu!!.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_in_favorites)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                hideKeyboard()
                viewModel.search(s.toString())

                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                return false
            }
        })

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                viewModel.clearSearch()
                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId != R.id.action_search) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TvShowInfoActivity.TVSHOW_INFO_REQUEST_CODE) {
            viewModel.handleFavoriteStatus(resultCode, data)
        }
    }
}