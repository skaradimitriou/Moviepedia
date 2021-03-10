package com.stathis.moviepedia.network

import com.stathis.moviepedia.models.UpcomingMovies
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApiClient {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val api: MoviepediaApi

    init {
        api = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviepediaApi::class.java)
    }

    fun getCountries(): Call<UpcomingMovies> {
        return api.getUpcomindMovies()
    }
}