package com.stathis.moviepedia.network

import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.models.MovieGenresFeed
import com.stathis.moviepedia.models.UpcomingMovies
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviepediaApi {

    @GET("movie/upcoming?$API_KEY")
    fun getUpcomindMovies() : Call<UpcomingMovies>

    @GET("trending/movie/day?$API_KEY")
    fun getTrendingMovies() : Call<UpcomingMovies>

    @GET("genre/movie/list?$API_KEY")
    fun getMovieGenres() : Call<MovieGenresFeed>

    @GET("movie/top_rated?$API_KEY")
    fun getTopRatedMovies() : Call<UpcomingMovies>

    @GET("search/multi?$API_KEY")
    fun getQueryInfo(@Query("query") query : String) : Call<SearchItemsFeed>
}