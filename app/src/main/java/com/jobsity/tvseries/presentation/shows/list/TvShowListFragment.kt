package com.jobsity.tvseries.presentation.shows.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.presentation.shows.TvShowAdapter
import com.jobsity.tvseries.util.message.JobsityMessage
import kotlinx.android.synthetic.main.error_try_again_view.*
import kotlinx.android.synthetic.main.shows_list_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TvShowListFragment : Fragment(R.layout.shows_list_view),
    TvShowAdapter.ITvShowAdapter {

    private val viewModel: TvShowListViewModel by viewModel()
    private val adapter = TvShowAdapter(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvShows.also {
            it.layoutManager = GridLayoutManager(activity, 3)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        viewModel.viewState.observe (viewLifecycleOwner, Observer { viewState ->
            pgShows.isVisible = viewState.isLoadingVisible
            rvShows.isVisible = viewState.isListVisible
            vError.isVisible = viewState.isTryAgainVisible
            txvErrorMessage.text = viewState.errorMessage
            btnTryAgain.isVisible = viewState.isTryAgainButtonVisible

            if (viewState.isErrorMessage) {
                JobsityMessage.showErrorMessage(activity, viewState.errorMessage)
            }

            rvShows.post { adapter.shows = viewState.tvShows }
        })

        btnTryAgain.setOnClickListener {
            viewModel.didClickOnTryAgain()
        }
    }

    override fun onReachLastItem() {
        viewModel.paginate()
    }

    override fun onClick(tvShow: TvShow) {

    }
}