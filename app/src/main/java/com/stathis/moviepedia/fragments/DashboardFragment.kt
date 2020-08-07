package com.stathis.moviepedia.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder

import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.models.MovieGenresFeed
import com.stathis.moviepedia.models.UpcomingMovies
import com.stathis.moviepedia.recyclerviews.GenresAdapter
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import com.stathis.moviepedia.recyclerviews.UpcomingMoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class DashboardFragment : Fragment() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                val upcomingMoviesRecView: RecyclerView = view!!.findViewById(R.id.upcomingMoviesRecView)

                activity!!.runOnUiThread {
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

                val popularRecView: RecyclerView = view!!.findViewById(R.id.popularRecView)
                //move from background to ui thread and display data
                activity!!.runOnUiThread {
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

                val genresRecView: RecyclerView = view!!.findViewById(R.id.genresRecView)
                   activity!!.runOnUiThread {
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

                val topRatedRecView: RecyclerView = view!!.findViewById(R.id.topRatedRecView)
                activity!!.runOnUiThread {
                    topRatedRecView.adapter = PopularMoviesAdapter(topRatedMovies)
                }

            }
        })
    }

}
