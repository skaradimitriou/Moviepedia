package com.stathis.moviepedia.fragments

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
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MoviesFragment : Fragment() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllMovies()
    }

    fun getAllMovies() {

        url = "https://api.themoviedb.org/3/trending/all/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
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
            }
        })
    }

}
