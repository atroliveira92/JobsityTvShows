package com.jobsity.tvseries.domain.model

import com.jobsity.tvseries.data.PeopleSearchData

object PersonMapper {

    fun map(peopleData: List<PeopleSearchData>): List<Person> {
        return peopleData.filter {
            it.person?.image != null
            && it.person.name != null
        }.map {
            Person(
                it.person!!.name!!,
         it.person.image!!.medium?: it.person.image.original
            )
        }
    }
}