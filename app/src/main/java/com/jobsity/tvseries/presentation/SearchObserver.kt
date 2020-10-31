package com.jobsity.tvseries.presentation

import com.jobsity.tvseries.presentation.SearchObserver.OnSearchPerform

class SearchObserver {
    private val list = mutableListOf<OnSearchPerform>()

    fun register(onSearch: OnSearchPerform) {
        list.add(onSearch)
    }

    fun unRegister(onSearch: SearchObserver.OnSearchPerform) {
        list.remove(onSearch)
    }

    fun notifySearch(value: String?) {
        for (onSearchPerform in list) {
            onSearchPerform.onSearch(value)
        }
    }

    interface OnSearchPerform {
        fun onSearch(value: String?)
    }
}