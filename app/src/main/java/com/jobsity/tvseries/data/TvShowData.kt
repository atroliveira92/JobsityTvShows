package com.jobsity.tvseries.data

data class TvShowData (
    var id: Int,
    var url: String?,
    var name: String?,
    var type: String?,
    var language: String?,
    var premiered: String?,
    var genres: List<String>?,
    var status: String?,
    var schedule: TvShowSchedule?,
    var image: TVShowImage?,
    var summary: String?,
    var rating: TVShowRating?
)

data class TvShowSchedule(
    var time: String?,
    var days: List<String>?
)

data class TVShowImage(
    var medium: String?,
    var original: String?
)

data class TVShowRating (
    var average: String?
)