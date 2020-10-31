package com.jobsity.tvseries.data

data class PersonData (
    val id: Int?,
    val url: String?,
    val name: String?,
    val image: PersonImage?
)

data class PersonImage(
    val medium: String?,
    val original: String?
)