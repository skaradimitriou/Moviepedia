package com.stathis.moviepedia.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object ApiClient {

    private lateinit var url: String
    private lateinit var request: Request
    private var client: OkHttpClient = OkHttpClient()
    val cast = MutableLiveData<MutableList<Cast>>()
    val reviews = MutableLiveData<MutableList<Reviews>>()

    fun getMovieCastInfo(movieId: Int) {
        url = "$BASE_URL/3/movie/$movieId/credits?$API_KEY"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val castFeed = GsonBuilder().create().fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", castFeed.toString())

                if (castFeed?.cast != null) {
                    cast.postValue(ArrayList(castFeed.cast))
                }
            }
        })
    }

    fun getMovieReviews(movieId: Int) {
        url = "$BASE_URL/3/movie/$movieId/reviews?$API_KEY"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val review = GsonBuilder().create().fromJson(body, ReviewsFeed::class.java)
                Log.d("Response", review.toString())

                if (review.results != null) {
                    reviews.postValue(ArrayList(review.results))
                }
            }
        })
    }

    fun getCastInfo(tvSeriesId: Int) {
        url = "$BASE_URL/3/tv/$tvSeriesId/credits?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val castFeed = gson.fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", castFeed.toString())

                if (castFeed.cast != null) {
                    cast.postValue(ArrayList(castFeed.cast))
                }
            }
        })
    }

    fun getTvSeriesReviews(tvSeriesId: Int) {
        url = "$BASE_URL/3/tv/$tvSeriesId/reviews?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val review = gson.fromJson(body, ReviewsFeed::class.java)
                Log.d("Response", review.toString())

                if (review.results != null) {
                    reviews.postValue(ArrayList(review.results))
                }
            }
        })
    }
}