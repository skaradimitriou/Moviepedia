package com.stathis.moviepedia.network

import com.stathis.moviepedia.models.MovieGenresFeed
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.TvSeriesFeed
import com.stathis.moviepedia.models.UpcomingMovies
import com.stathis.moviepedia.models.actor.Actor
import com.stathis.moviepedia.models.actor.KnownMoviesFeed
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path

object ApiClient {

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

    fun getTrendingMovies(): Call<UpcomingMovies> {
        return api.getTrendingMovies()
    }

    fun getMovieGenres(): Call<MovieGenresFeed> {
        return api.getMovieGenres()
    }

    fun getTopRatedMovies(): Call<UpcomingMovies> {
        return api.getTopRatedMovies()
    }

    fun getQueryInfo(query: String): Call<SearchItemsFeed> {
        return api.getQueryInfo(query)
    }

    fun getFeaturedTvSeries(): Call<TvSeriesFeed> {
        return api.getFeaturedTvSeries()
    }

    fun getAiringTodayTvSeries(): Call<TvSeriesFeed> {
        return api.getAiringTodayTvSeries()
    }

    fun getTopRatedTvSeries(): Call<TvSeriesFeed> {
        return api.getTopRatedTvSeries()
    }

    fun getPopularTvSeries(): Call<TvSeriesFeed> {
        return api.getPopularTvSeries()
    }

    fun getTvGenres(): Call<MovieGenresFeed> {
        return api.getTvGenres()
    }

    fun getResultsForThisGenre(query: Int): Call<UpcomingMovies> {
        return api.getResultsForThisGenre(query)
    }

    fun getMovieCastInfo(movieId: Int): Call<MovieCastFeed> {
        return api.getMovieCastInfo(movieId)
    }

    fun getMovieReviews(movieId: Int): Call<ReviewsFeed> {
        return api.getMovieReviews(movieId)
    }

    fun getTvCastInfo(tvSeriesId: Int): Call<MovieCastFeed> {
        return api.getTvCastInfo(tvSeriesId)
    }

    fun getTvReviews(tvSeriesId: Int): Call<ReviewsFeed> {
        return api.getTvReviews(tvSeriesId)
    }

    fun getActorInfo(actorId: Int): Call<Actor> {
        return api.getActorInfo(actorId)
    }

    fun getActorsKnownMovies(actorId: Int): Call<KnownMoviesFeed> {
        return api.getActorsKnownMovies(actorId)
    }
}