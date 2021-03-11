package com.stathis.moviepedia.ui.dashboard.fragments.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.network.RetrofitApiClient
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MoviesRepository {

    val upcomingMovies = MutableLiveData<List<Movies>>()
    val trendingMovies = MutableLiveData<List<Movies>>()
    val movieGenres = MutableLiveData<List<MovieGenres>>()
    val topRatedMovies = MutableLiveData<List<Movies>>()

    fun getUpcomingMovies() {
        RetrofitApiClient.getCountries().enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: Call<UpcomingMovies>,
                response: Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                upcomingMovies.postValue(response.body()?.results)
            }

            override fun onFailure(call: Call<UpcomingMovies>, t: Throwable) {
                upcomingMovies.postValue(null)
            }
        })
    }

    fun getTrendingMovies() {
        RetrofitApiClient.getTrendingMovies().enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: Call<UpcomingMovies>,
                response: Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                trendingMovies.postValue(response.body()?.results)
            }

            override fun onFailure(call: Call<UpcomingMovies>, t: Throwable) {
                trendingMovies.postValue(null)
            }
        })
    }

    fun getMovieGenres() {
        RetrofitApiClient.getMovieGenres().enqueue(object : Callback<MovieGenresFeed> {
            override fun onResponse(
                call: Call<MovieGenresFeed>,
                response: Response<MovieGenresFeed>
            ) {
                Log.d("", response.body().toString())
                movieGenres.postValue(response.body()?.genres)
            }

            override fun onFailure(call: Call<MovieGenresFeed>, t: Throwable) {
                movieGenres.postValue(null)
            }
        })
    }

    fun getTopRatedMovies() {
        RetrofitApiClient.getTopRatedMovies().enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: Call<UpcomingMovies>,
                response: Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                topRatedMovies.postValue(response.body()?.results)
            }

            override fun onFailure(call: Call<UpcomingMovies>, t: Throwable) {
                topRatedMovies.postValue(null)
            }
        })
    }
}