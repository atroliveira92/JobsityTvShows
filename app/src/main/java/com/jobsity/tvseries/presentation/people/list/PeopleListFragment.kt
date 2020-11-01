package com.jobsity.tvseries.presentation.people.list

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jobsity.tvseries.R
import com.jobsity.tvseries.presentation.SearchObserver
import com.jobsity.tvseries.presentation.SearchObserver.*

import com.jobsity.tvseries.util.message.JobsityMessage
import kotlinx.android.synthetic.main.error_try_again_view.*
import kotlinx.android.synthetic.main.shows_list_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class PeopleListFragment(var searchObserver: SearchObserver): Fragment(R.layout.shows_list_view), OnSearchPerform {

    private val viewModel: PeopleListViewModel by viewModel()
    private val adapter =
        PeopleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchObserver.register(this)

        rvShows.also {
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            it.layoutManager = layoutManager
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        imgvError.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_baseline_people_24))

        viewModel.viewState.observe (viewLifecycleOwner, Observer { viewState ->
            pgShows.isVisible = viewState.isLoadingVisible
            rvShows.isVisible = viewState.isListVisible
            vError.isVisible = viewState.isTryAgainVisible
            txvErrorMessage.text = viewState.errorMessage
            btnTryAgain.isVisible = viewState.isTryAgainButtonVisible

            if (viewState.isErrorMessage) {
                JobsityMessage.showErrorMessage(activity, viewState.errorMessage)
            }

            rvShows.post { adapter.people = viewState.people }
        })

        btnTryAgain.setOnClickListener {
            viewModel.didClickOnTryAgain()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchObserver.unRegister(this)
    }

    override fun onSearch(value: String?) {
        viewModel.search(value)
    }
}