package com.stathis.moviepedia.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.models.actor.Actor
import com.stathis.moviepedia.models.actor.KnownMoviesFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path

object ApiClient {

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
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

    fun getMovieCastInfo(movieId: Int, data : MutableLiveData<List<Cast>>, error : MutableLiveData<Boolean>) {
        api.getMovieCastInfo(movieId).enqueue(object : Callback<MovieCastFeed> {
            override fun onResponse(call: Call<MovieCastFeed>,response: Response<MovieCastFeed>) {
                data.postValue(response.body()?.cast)
                error.postValue(false)
            }

            override fun onFailure(call: Call<MovieCastFeed>, t: Throwable) {
                error.postValue(true)
            }
        })
    }

    fun getMovieReviews(movieId: Int,data : MutableLiveData<List<Reviews>>, error : MutableLiveData<Boolean>) {
        api.getMovieReviews(movieId).enqueue(object : Callback<ReviewsFeed> {
            override fun onResponse(call: Call<ReviewsFeed>, response: Response<ReviewsFeed>) {
                data.postValue(response.body()?.results)
                error.postValue(false)
            }

            override fun onFailure(call: Call<ReviewsFeed>, t: Throwable) {
                error.postValue(true)
            }
        })
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