package com.stathis.moviepedia.network

import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.models.UpcomingMovies
import retrofit2.Call
import retrofit2.http.GET

interface MoviepediaApi {

    @GET("movie/upcoming?$API_KEY")
    fun getUpcomindMovies() : Call<UpcomingMovies>
}