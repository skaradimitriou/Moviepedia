package com.stathis.moviepedia.ui.dashboard.fragments.tvSeries

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

class TvSeriesViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private val apiKey = "b36812048cc4b54d559f16a2ff196bc5"
    private var featuredTvSeries : MutableLiveData<MutableList<TvSeries>> = MutableLiveData()
    private var airingToday : MutableLiveData<MutableList<TvSeries>> = MutableLiveData()
    private var topRated : MutableLiveData<MutableList<TvSeries>> = MutableLiveData()
    private var popularTvSeries : MutableLiveData<MutableList<TvSeries>> = MutableLiveData()
    private var tvSeriesGenres : MutableLiveData<MutableList<MovieGenres>> = MutableLiveData()
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

    fun getFeaturedTvSeries(): MutableLiveData<MutableList<TvSeries>> {
        url = "https://api.themoviedb.org/3/tv/on_the_air?api_key=$apiKey"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()

                val response =
                    gson.fromJson(response.body?.string(), TvSeriesFeed::class.java)
                //converts List to ArrayList<TvSeries>
                val featuredTvSeriesArray: MutableList<TvSeries> = ArrayList(response.results)
                Log.d("Response", featuredTvSeriesArray.toString())
                featuredTvSeries.postValue(featuredTvSeriesArray)
//
            }

        })
        return featuredTvSeries
    }

    fun getAiringTodayTvSeries(): MutableLiveData<MutableList<TvSeries>> {
        url =
            "https://api.themoviedb.org/3/tv/airing_today?api_key=$apiKey"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                val response = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", response.toString())

                val airingTodayTvSeries:MutableList<TvSeries> = ArrayList(response.results)
                airingToday.postValue(airingTodayTvSeries)
            }
        })
        return airingToday
    }

    fun getTopRatedTvSeries(): MutableLiveData<MutableList<TvSeries>> {
        url = "https://api.themoviedb.org/3/tv/top_rated?api_key=$apiKey"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                //converting body response from JSON to Kotlin class
                val topRatedTvSeries = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", topRatedTvSeries.toString())

                val response:MutableList<TvSeries> = ArrayList(topRatedTvSeries.results)
                topRated.postValue(response)
            }
        })
        return topRated
    }

    fun getPopularTvSeries(): MutableLiveData<MutableList<TvSeries>> {
        url =
            "https://api.themoviedb.org/3/tv/top_rated?api_key=$apiKey&language=en-US"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()

                //converting body response from JSON to Kotlin class
                val response = gson.fromJson(body, TvSeriesFeed::class.java)
                Log.d("Response", response.toString())

                val popularTvSeriesArray = ArrayList(response.results)

                popularTvSeries.postValue(popularTvSeriesArray)
            }
        })
        return popularTvSeries
    }

    fun getTvGenres(): MutableLiveData<MutableList<MovieGenres>> {
        url =
            "https://api.themoviedb.org/3/genre/tv/list?api_key=$apiKey"
        request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Genre call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val movieGenres = gson.fromJson(body, MovieGenresFeed::class.java)
                Log.d("RESPONSE", movieGenres.toString())
                val genresList = ArrayList(movieGenres.genres)
                tvSeriesGenres.postValue(genresList)
            }
        })
        return tvSeriesGenres
    }

}