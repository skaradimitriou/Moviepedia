package com.stathis.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.*
import com.stathis.moviepedia.recyclerviews.GenresAdapter
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import com.stathis.moviepedia.recyclerviews.UpcomingMoviesAdapter
import okhttp3.Call
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URI.create

class Dashboard : AppCompatActivity() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        getUpcomingMovies()
        getTrendingMovies()
        getMovieGenres()
        getTopRatedMovies()
    }

    fun getUpcomingMovies() {

        url = "https://api.themoviedb.org/3/movie/upcoming?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val upcomingMovies = gson.fromJson(body, UpcomingMovies::class.java)
                Log.d("Response", upcomingMovies.toString())

                val upcomingMoviesRecView: RecyclerView = findViewById(R.id.upcomingMoviesRecView)

                runOnUiThread {
                    upcomingMoviesRecView.adapter = UpcomingMoviesAdapter(upcomingMovies)
                }
            }
        })
    }

    fun getTrendingMovies() {

        url =
            "https://api.themoviedb.org/3/trending/all/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val popularMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Response", popularMovies.toString())

                val popularRecView: RecyclerView = findViewById(R.id.popularRecView)

                runOnUiThread {
                    popularRecView.adapter = PopularMoviesAdapter(popularMovies)
                }
            }
        })
    }

    fun getMovieGenres() {

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
                println(body)

                val gson = GsonBuilder().create()
                val movieGenres = gson.fromJson(body, MovieGenresFeed::class.java)
                Log.d("RESPONSE", movieGenres.toString())

                val genresRecView: RecyclerView = findViewById(R.id.genresRecView)

                runOnUiThread {
                    genresRecView.adapter = GenresAdapter(movieGenres)
                }
            }
        })
    }

    fun getTopRatedMovies() {
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
                val topRatedMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Top Rated Call Response", topRatedMovies.toString())

                val topRatedRecView: RecyclerView = findViewById(R.id.topRatedRecView)

                runOnUiThread {
                    topRatedRecView.adapter = PopularMoviesAdapter(topRatedMovies)
                }
            }
        })
    }
}
