package com.stathis.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.recyclerviews.PopularMoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        fetchJson()

    }

    fun fetchJson() {
        println("Attempting to fetch JSON from Tmdb")

        val url =
            "https://api.themoviedb.org/3/trending/all/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val popularMovies = gson.fromJson(body, MovieFeed::class.java)
                Log.d("Response", popularMovies.toString())

                val popularRecView: RecyclerView = findViewById(R.id.popularRecView)

                runOnUiThread{
                    popularRecView.adapter = PopularMoviesAdapter(popularMovies)
                }
            }

        })
    }


}
