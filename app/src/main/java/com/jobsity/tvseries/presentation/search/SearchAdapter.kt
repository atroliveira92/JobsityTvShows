package com.jobsity.tvseries.presentation.search

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jobsity.tvseries.presentation.SearchObserver
import com.jobsity.tvseries.presentation.people.list.PeopleListFragment
import com.jobsity.tvseries.presentation.shows.search.TvShowsSearchFragment

class SearchAdapter(private val myContext: Context,
                    fm: FragmentManager,
                    val searchObserver: SearchObserver,
                    internal var totalTabs: Int) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> {
                PeopleListFragment(
                    searchObserver
                )
            }
            else -> TvShowsSearchFragment(searchObserver)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}