package com.stathis.moviepedia.ui.dashboard.fragments.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MoviesRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private val client = OkHttpClient()
    val upcomingMovies = MutableLiveData<List<Movies>>()
    val trendingMovies = MutableLiveData<List<Movies>>()
    val movieGenres = MutableLiveData<List<MovieGenres>>()
    val topRatedMovies = MutableLiveData<List<Movies>>()

    fun getUpcomingMovies() {
        url = "$BASE_URL/movie/upcoming?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val upcomingMovie =
                    GsonBuilder().create().fromJson(body, UpcomingMovies::class.java)
                upcomingMovies.postValue(upcomingMovie.results)
            }
        })
    }

    fun getTrendingMovies() {
        url = "$BASE_URL/trending/movie/day?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val popularMovies = GsonBuilder().create().fromJson(body, MovieFeed::class.java)
                trendingMovies.postValue(popularMovies.results)
            }
        })
    }

    fun getMovieGenres() {
        url = "$BASE_URL/genre/movie/list?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val movieGenre = GsonBuilder().create().fromJson(body, MovieGenresFeed::class.java)
                movieGenres.postValue(movieGenre.genres)
            }
        })
    }

    fun getTopRatedMovies() {
        url = "$BASE_URL/movie/top_rated?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Top Rated Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val topRatedMovie = GsonBuilder().create().fromJson(body, MovieFeed::class.java)
                topRatedMovies.postValue(topRatedMovie.results)
            }
        })
    }
}