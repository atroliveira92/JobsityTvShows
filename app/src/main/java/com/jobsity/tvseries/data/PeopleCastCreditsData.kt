package com.jobsity.tvseries.data

data class PeopleCastCreditsData (
    val _embedded: EmbeddedShow?
)
data class EmbeddedShow(
    val show: TvShowData?
)
