package com.jobsity.tvseries.domain.repository

import com.jobsity.tvseries.domain.model.Person
import com.jobsity.tvseries.domain.model.PersonMapper
import com.jobsity.tvseries.domain.model.TvShow
import com.jobsity.tvseries.domain.model.TvShowMapper
import com.jobsity.tvseries.domain.network.SafeApiRequest
import com.jobsity.tvseries.domain.network.TVShowAPI
import com.jobsity.tvseries.util.exception.JobsityListException

class PeopleRepository(private val api: TVShowAPI): SafeApiRequest() {

    suspend fun searchPeople(searchTerm: String): List<Person> {
        val listData = apiRequest { api.searchPeople(searchTerm) }
        val list = PersonMapper.map(listData)

        if (list.isEmpty()) {
            throw JobsityListException.EmptyDataListException("data is empty")
        }

        return list
    }
    
    suspend fun loadPersonShows(id: Int): List<TvShow> {
        val listData = apiRequest { api.loadPersonShows(id.toString()) }
        val list = TvShowMapper.mapEmbeddedShowLists(listData)

        if (list.isEmpty()) {
            throw JobsityListException.EmptyDataListException("data is empty")
        }

        return list
    }
}