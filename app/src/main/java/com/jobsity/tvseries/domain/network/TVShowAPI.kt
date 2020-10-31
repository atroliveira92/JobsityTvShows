package com.jobsity.tvseries.domain.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jobsity.tvseries.data.PeopleSearchData
import com.jobsity.tvseries.data.TvShowData
import com.jobsity.tvseries.data.TvShowEpisodeData
import com.jobsity.tvseries.data.TvShowSearchData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVShowAPI {

    @GET("/shows")
    suspend fun getShows(@Query("page") page: Int): Response<List<TvShowData>>

    @GET("/search/shows")
    suspend fun searchShows(@Query("q") search: String): Response<List<TvShowSearchData>>

    @GET("/search/people")
    suspend fun searchPeople(@Query("q") search: String): Response<List<PeopleSearchData>>

    @GET("/shows/{id}/episodes")
    suspend fun loadEpisodes(@Path("id") id: String): Response<List<TvShowEpisodeData>>

    companion object {
        const val BASE_URL = "http://api.tvmaze.com"
        operator fun invoke(netWorkConnectionInterceptor: NetWorkConnectionInterceptor) : TVShowAPI {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
                .addInterceptor(netWorkConnectionInterceptor)

            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
                .create(TVShowAPI::class.java)
        }
    }
}