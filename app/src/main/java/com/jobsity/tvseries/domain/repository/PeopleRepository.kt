package com.jobsity.tvseries.domain.repository

import com.jobsity.tvseries.domain.model.Person
import com.jobsity.tvseries.domain.model.PersonMapper
import com.jobsity.tvseries.domain.network.SafeApiRequest
import com.jobsity.tvseries.domain.network.TVShowAPI
import com.jobsity.tvseries.util.exception.JobsityException

class PeopleRepository(private val api: TVShowAPI): SafeApiRequest() {

    suspend fun searchPeople(searchTerm: String): List<Person> {
        val listData = apiRequest { api.searchPeople(searchTerm) }
        val list = PersonMapper.map(listData)

        if (list.isEmpty()) {
            throw JobsityException.EmptyDataException("data is empty")
        }

        return list
    }
}