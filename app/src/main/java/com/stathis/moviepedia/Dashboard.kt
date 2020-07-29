package com.stathis.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.MovieFeed
import com.stathis.moviepedia.models.Movies
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.stream.DoubleStream.builder

class Dashboard : AppCompatActivity() {

    val MOVIE_URL = "https://api.themoviedb.org/3/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        fetchJson()
    }

    fun fetchJson(){
        println("Attempting to fetch JSON from Tmdb")

        val url = "https://api.themoviedb.org/3/trending/all/day?api_key=b36812048cc4b54d559f16a2ff196bc5"
        val request = okhttp3.Request.Builder().url(url).build()

        val client =  OkHttpClient()
        client.newCall(request).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("YOU FAILED",call.toString())
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val movieFeed = gson.fromJson(body,MovieFeed::class.java)
                Log.d("Response",movieFeed.toString())
            }

        })
    }



}
