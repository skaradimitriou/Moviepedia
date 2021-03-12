package com.stathis.moviepedia.network

import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.models.MovieGenresFeed
import com.stathis.moviepedia.models.TvSeriesFeed
import com.stathis.moviepedia.models.UpcomingMovies
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviepediaApi {

    @GET("movie/upcoming?$API_KEY")
    fun getUpcomindMovies(): Call<UpcomingMovies>

    @GET("trending/movie/day?$API_KEY")
    fun getTrendingMovies(): Call<UpcomingMovies>

    @GET("genre/movie/list?$API_KEY")
    fun getMovieGenres(): Call<MovieGenresFeed>

    @GET("movie/top_rated?$API_KEY")
    fun getTopRatedMovies(): Call<UpcomingMovies>

    @GET("tv/on_the_air?$API_KEY")
    fun getFeaturedTvSeries(): Call<TvSeriesFeed>

    @GET("tv/airing_today?$API_KEY")
    fun getAiringTodayTvSeries(): Call<TvSeriesFeed>

    @GET("tv/top_rated?$API_KEY")
    fun getTopRatedTvSeries(): Call<TvSeriesFeed>

    @GET("tv/top_rated?$API_KEY&language=en-US")
    fun getPopularTvSeries(): Call<TvSeriesFeed>

    @GET("genre/tv/list?$API_KEY")
    fun getTvGenres(): Call<MovieGenresFeed>

    @GET("search/multi?$API_KEY")
    fun getQueryInfo(@Query("query") query: String): Call<SearchItemsFeed>

    @GET("discover/movie?$API_KEY&with_genres=")
    fun getResultsForThisGenre(@Query("queryId") queryId: Int): Call<UpcomingMovies>
}