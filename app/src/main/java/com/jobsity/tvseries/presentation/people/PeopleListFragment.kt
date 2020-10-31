package com.jobsity.tvseries.presentation.people

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.presentation.SearchObserver
import com.jobsity.tvseries.presentation.SearchObserver.*
import com.jobsity.tvseries.presentation.shows.TvShowAdapter.*


class PeopleListFragment(var searchObserver: SearchObserver): Fragment(R.layout.people_list_view), OnSearchPerform {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchObserver.register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchObserver.unRegister(this)
    }

    override fun onSearch(value: String?) {

    }

}