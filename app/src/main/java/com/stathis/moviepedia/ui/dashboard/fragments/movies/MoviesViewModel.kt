package com.stathis.moviepedia.ui.dashboard.fragments.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MoviesViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var upcomingMoviesList: MutableList<Movies> = mutableListOf()
    private var trendingMoviesList: MutableList<Movies> = mutableListOf()
    private var genresList: MutableList<MovieGenres> = mutableListOf()
    private var topRatedMoviesList: MutableList<Movies> = mutableListOf()
    private var upcomingMovies : MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private var trendingMovies : MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private var movieGenres : MutableLiveData<MutableList<MovieGenres>> = MutableLiveData()
    private var topRatedMovies : MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private var emptyModelList: MutableList<EmptyModel> = mutableListOf()

    init{
        setShimmer()
    }

    fun setShimmer(): MutableList<EmptyModel> {
        val emptyModel = EmptyModel("")
        emptyModelList.add(emptyModel)
        emptyModelList.add(emptyModel)
        emptyModelList.add(emptyModel)
        emptyModelList.add(emptyModel)
        return emptyModelList
    }

    fun getUpcomingMovies(): MutableLiveData<MutableList<Movies>> {
        url = "https://api.themoviedb.org/3/movie/upcoming?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val upcomingMovie = gson.fromJson(body, UpcomingMovies::class.java)
                Log.d("Response", upcomingMovies.toString())
                upcomingMoviesList = ArrayList(upcomingMovie.results)

                Log.d("this is the list", upcomingMoviesList.toString())
                upcomingMovies.postValue(upcomingMoviesList)
            }
        })
        return upcomingMovies
    }

    fun getTrendingMovies(): MutableLiveData<MutableList<Movies>> {
        url =
            "https://api.themoviedb.org/3/trending/movie/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val popularMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Response", popularMovies.toString())

                trendingMoviesList = ArrayList(popularMovies.results)
                Log.d("this is the list", trendingMoviesList.toString())

                //move from background to ui thread and display data
                trendingMovies.postValue(trendingMoviesList)
            }
        })
        return trendingMovies
    }

    fun getMovieGenres(): MutableLiveData<MutableList<MovieGenres>> {
        url =
            "https://api.themoviedb.org/3/genre/movie/list?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val movieGenre = gson.fromJson(body, MovieGenresFeed::class.java)
                Log.d("RESPONSE", movieGenres.toString())
                genresList = ArrayList(movieGenre.genres)

                movieGenres.postValue(genresList)
            }
        })
        return movieGenres
    }

    fun getTopRatedMovies(): MutableLiveData<MutableList<Movies>> {
        url =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Top Rated Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.d("TOP RATED MOVIES CALL", body.toString())
                val gson = GsonBuilder().create()
                val topRatedMovie = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Top Rated Call Response", topRatedMovies.toString())

                topRatedMoviesList = ArrayList(topRatedMovie.results)
                Log.d("this is the list", topRatedMoviesList.toString())

                topRatedMovies.postValue(topRatedMoviesList)
            }
        })
        return topRatedMovies
    }

}