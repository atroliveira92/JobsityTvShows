package com.jobsity.tvseries.domain.model

data class TvShow (
    val name: String,
    val posterHigh: String?,
    val posterMedium: String,
    val time: String,
    val days: List<String>,
    val genre: List<String>?,
    val summary: String,
    val rating: String?,
    val premier: String?
)