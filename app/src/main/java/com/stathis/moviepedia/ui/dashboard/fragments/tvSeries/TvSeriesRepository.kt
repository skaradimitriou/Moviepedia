package com.stathis.moviepedia.ui.dashboard.fragments.tvSeries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.MovieGenres
import com.stathis.moviepedia.models.MovieGenresFeed
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.models.TvSeriesFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TvSeriesRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private val client = OkHttpClient()
    val featuredTvSeries = MutableLiveData<MutableList<TvSeries>>()
    val airingToday = MutableLiveData<MutableList<TvSeries>>()
    val topRated = MutableLiveData<MutableList<TvSeries>>()
    val popularTvSeries = MutableLiveData<MutableList<TvSeries>>()
    val tvSeriesGenres = MutableLiveData<MutableList<MovieGenres>>()

    fun getFeaturedTvSeries() {
        url = "$BASE_URL/tv/on_the_air?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                val response =
                    gson.fromJson(response.body?.string(), TvSeriesFeed::class.java)
                featuredTvSeries.postValue(ArrayList(response.results))
            }
        })
    }

    fun getAiringTodayTvSeries() {
        url = "$BASE_URL/tv/airing_today?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                val response = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", response.toString())

                val airingTodayTvSeries: MutableList<TvSeries> = ArrayList(response.results)
                airingToday.postValue(airingTodayTvSeries)
            }
        })
    }

    fun getTopRatedTvSeries() {
        url = "$BASE_URL/tv/top_rated?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                val topRatedTvSeries = gson.fromJson(response.body?.string(), TvSeriesFeed::class.java)
                topRated.postValue(ArrayList(topRatedTvSeries.results))
            }
        })
    }

    fun getPopularTvSeries() {
        url = "$BASE_URL/tv/top_rated?$API_KEY&language=en-US"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                val response = gson.fromJson(response.body?.string(), TvSeriesFeed::class.java)
                popularTvSeries.postValue(ArrayList(response.results))
            }
        })
    }

    fun getTvGenres() {
        url = "$BASE_URL/genre/tv/list?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                val movieGenres = gson.fromJson(response.body?.string(), MovieGenresFeed::class.java)
                tvSeriesGenres.postValue(ArrayList(movieGenres.genres))
            }
        })
    }
}