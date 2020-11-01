package com.jobsity.tvseries.presentation.shows.favorites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.presentation.shows.TvShowAdapter
import com.jobsity.tvseries.presentation.shows.TvShowAdapter.ITvShowAdapter
import com.jobsity.tvseries.presentation.shows.info.TvShowInfoActivity
import com.jobsity.tvseries.util.message.JobsityMessage
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.error_try_again_view.*
import kotlinx.android.synthetic.main.favorites_view.*
import kotlinx.android.synthetic.main.favorites_view.searchBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTvShowsActivity: AppCompatActivity(R.layout.favorites_view), ITvShowAdapter, MaterialSearchBar.OnSearchActionListener {

    private val viewModel: FavoritesTvShowViewModel by viewModel()
    private val adapter = TvShowAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchBar.setSpeechMode(false)
        searchBar.setOnSearchActionListener(this)

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

        imgvBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onReachLastItem() {}

    override fun onClick(tvShow: TvShow) {
        viewModel.didClickOnTvShow(tvShow)
    }

    override fun onButtonClicked(buttonCode: Int) {}

    override fun onSearchStateChanged(enabled: Boolean) {
        imgvBack.isVisible = !enabled
        if (!enabled) {
            viewModel.clearSearch()
        }
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        viewModel.search(text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TvShowInfoActivity.TVSHOW_INFO_REQUEST_CODE) {
            viewModel.handleFavoriteStatus(resultCode, data)
        }
    }
}