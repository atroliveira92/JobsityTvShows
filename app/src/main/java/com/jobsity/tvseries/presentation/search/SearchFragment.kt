package com.jobsity.tvseries.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.jobsity.tvseries.R
import com.jobsity.tvseries.presentation.SearchObserver
import kotlinx.android.synthetic.main.search_view.*

class SearchFragment(val searchObserver: SearchObserver): Fragment(R.layout.search_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout.addTab(tabLayout!!.newTab().setText("Shows"))
        tabLayout.addTab(tabLayout!!.newTab().setText("People"))

        val adapter = SearchAdapter(activity!!, childFragmentManager, searchObserver, tabLayout!!.tabCount)
        viewPager.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}