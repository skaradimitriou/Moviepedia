package com.stathis.moviepedia.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.actor.Actor
import com.stathis.moviepedia.models.actor.KnownMoviesFeed
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.models.genres.MovieGenresFeed
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.movies.UpcomingMovies
import com.stathis.moviepedia.models.reviews.ReviewsFeed
import com.stathis.moviepedia.models.series.TvSeriesFeed
import com.stathis.moviepedia.ui.dashboard.search.models.SearchItemsFeed
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviepediaApi::class.java)

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

    suspend fun getQueryInfo(query: String): Response<SearchItemsFeed> {
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

    fun getResultsForThisGenre(genreId: Int, data : MutableLiveData<List<Movies>>, error : MutableLiveData<Boolean>) {
        api.getResultsForThisGenre(genreId).enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(call: Call<UpcomingMovies>, response: retrofit2.Response<UpcomingMovies>) {
                Log.d("", response.body().toString())
                data.postValue(response.body()?.results)
                error.postValue(false)
            }

            override fun onFailure(call: Call<UpcomingMovies>, t: Throwable) {
                error.postValue(true)
            }
        })
    }

    suspend fun getMovieCastInfo(movieId: Int) : Response<MovieCastFeed> {
        return api.getMovieCastInfo(movieId)
    }

    suspend fun getMovieReviews(movieId: Int) : Response<ReviewsFeed> {
        return api.getMovieReviews(movieId)
    }

    suspend fun getTvCastInfo(tvSeriesId: Int): Response<MovieCastFeed> {
        return api.getTvCastInfo(tvSeriesId)
    }

    suspend fun getTvReviews(tvSeriesId: Int): Response<ReviewsFeed> {
        return api.getTvReviews(tvSeriesId)
    }

    fun getActorInfo(actorId: Int): Call<Actor> {
        return api.getActorInfo(actorId)
    }

    fun getActorsKnownMovies(actorId: Int): Call<KnownMoviesFeed> {
        return api.getActorsKnownMovies(actorId)
    }
}